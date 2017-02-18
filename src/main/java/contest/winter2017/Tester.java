package contest.winter2017;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICounter;
import org.jacoco.core.data.ExecutionData;
import org.jacoco.core.data.ExecutionDataReader;
import org.jacoco.core.data.IExecutionDataVisitor;
import org.jacoco.core.data.ISessionInfoVisitor;
import org.jacoco.core.data.SessionInfo;
import org.jacoco.core.tools.ExecFileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import contest.winter2017.parameter.Generator;
import contest.winter2017.util.PatternRecognizer;

/**
 * Class that will handle execution of basic tests and exploratory security test
 * on a black-box executable jar.
 *
 * Example code that we used to guide our use of Jacoco code coverage was
 * found @ http://www.eclemma.org/jacoco/trunk/doc/api.html
 *
 * @author IDT
 */
public class Tester {
	public static final String TESTS_KEY = "tests";

	/**
	 * horizontal line shown between test output
	 */
	public static final String HORIZONTAL_LINE = "-------------------------------------------------------------------------------------------";

	/**
	 * suffix for all jacoco output files
	 */
	private static final String JACOCO_OUTPUT_FILE_SUFFIX = "_jacoco.exec";

	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * path of the jar to test as a String
	 */
	private String jarToTestPath = null;

	/**
	 * path of the directory for jacoco output as a String
	 */
	private String jacocoOutputDirPath = null;

	/**
	 * path to the jacoco agent library as a String
	 */
	private String jacocoAgentJarPath = null;

	/**
	 * path to the file for jacoco output as a String
	 */
	private String jacocoOutputFilePath = null;

	/**
	 * number of exploratory black box tests to run.
	 */
	private int bbTests = 1000;

	/**
	 * number of seconds to run. 0 means no time limit.
	 */
	private long timeGoal = 5 * 60; // 5 minutes

	/**
	 * basic tests that have been extracted from the jar under test
	 */
	private List<Test> tests = null;

	/**
	 * parameter factory that can be used to help figure out parameter
	 * signatures from the blackbox jars
	 */
	private ParameterFactory parameterFactory = null;

	private volatile boolean timeUp = false;

	private final long ONE_RUN_SECONDS = 10;

	private int total = 0;
	private int failed = 0;
	private int passed = 0;

	PatternRecognizer ai = new PatternRecognizer();

	//////////////////////////////////////////
	// PUBLIC METHODS
	//////////////////////////////////////////

	/**
	 * Method that will initialize the Framework by loading up the jar to test,
	 * and then extracting parameters, parameter bounds (if any), and basic
	 * tests from the jar.
	 *
	 * @param initJarToTestPath
	 *            - String representing path of the jar to test
	 * @param initJacocoOutputDirPath
	 *            - String representing path of the directory jacoco will use
	 *            for output
	 * @param initJacocoAgentJarPath
	 *            - String representing path of the jacoco agent jar
	 * @param bbTests
	 *            - basic tests that have been extracted from the jar under test
	 * @param timeGoal
	 *            - number of seconds to run
	 * @return boolean - false if initialization encounters an Exception, true
	 *         if it does not
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean init(String initJarToTestPath, String initJacocoOutputDirPath, String initJacocoAgentJarPath,
			Integer bbTests, Integer timeGoal) {
		this.jarToTestPath = initJarToTestPath;
		this.jacocoOutputDirPath = initJacocoOutputDirPath;
		this.jacocoAgentJarPath = initJacocoAgentJarPath;
		if (bbTests != null) {
			int bbTestsVal = bbTests.intValue();
			Preconditions.checkArgument(bbTestsVal > 0, "bbTests should be greater than zero");
			this.bbTests = bbTestsVal;
		}
		if (timeGoal != null) {
			this.timeGoal = timeGoal.intValue();
		}

		File jarFileToTest = new File(this.jarToTestPath);
		this.jacocoOutputFilePath = this.jacocoOutputDirPath + "\\" + jarFileToTest.getName().replaceAll("\\.", "_")
				+ JACOCO_OUTPUT_FILE_SUFFIX;

		File jacocoOutputFile = new File(this.jacocoOutputFilePath);
		if (jacocoOutputFile != null && jacocoOutputFile.exists()) {
			jacocoOutputFile.delete();
		}

		URL fileURL = null;
		URL jarURL = null;
		try {

			// load up the jar under test so that we can access information
			// about the class from 'TestBounds'
			fileURL = jarFileToTest.toURI().toURL();
			String jarUrlTemp = "jar:" + jarFileToTest.toURI().toString() + "!/";
			jarURL = new URL(jarUrlTemp);
			URLClassLoader cl = URLClassLoader.newInstance(new URL[] { fileURL });
			JarURLConnection jarURLconn = null;
			jarURLconn = (JarURLConnection) jarURL.openConnection();

			// figuring out where the entry-point (main class) is in the jar
			// under test
			Attributes attr = null;
			attr = jarURLconn.getMainAttributes();
			String mainClassName = attr.getValue(Attributes.Name.MAIN_CLASS);

			// loading the TestBounds class from the jar under test
			String mainClassTestBoundsName = mainClassName + "TestBounds";
			Class<?> mainClassTestBounds = null;
			try {
				mainClassTestBounds = cl.loadClass(mainClassTestBoundsName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			// use reflection to invoke the TestBounds class to get the usage
			// information from the jar
			Method testBoundsMethod = null;
			testBoundsMethod = mainClassTestBounds.getMethod("testBounds");

			Object mainClassTestBoundsInstance = null;
			mainClassTestBoundsInstance = mainClassTestBounds.newInstance();

			Map<String, Object> mainClassTestBoundsMap = null;
			mainClassTestBoundsMap = (Map<String, Object>) testBoundsMethod.invoke(mainClassTestBoundsInstance);

			// instantiating a new Parameter Factory using the Test Bounds map
			this.parameterFactory = new ParameterFactory(mainClassTestBoundsMap);

			// get a list of basic tests from the TestBounds class
			this.tests = new ArrayList<Test>();
			List testList = (List) mainClassTestBoundsMap.get(TESTS_KEY);
			for (Object inTest : testList) {
				this.tests.add(new Test((Map) inTest));
			}

			extractExternalTests();
		} catch (IOException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| InstantiationException | NoSuchMethodException | SecurityException | NullPointerException e) {
			// if we have an exception during initialization, display the error
			// to the user and return a false status
			System.out.println("ERROR: An exception occurred during initialization.");
			e.printStackTrace();
			return false;
		}

		// if we did not encounter an exception during initialization, return a
		// true status
		return true;
	}

	/**
	 * This is the half of the framework that IDT has completed. We are able to
	 * pull basic tests directly from the executable jar. We are able to run the
	 * tests and assess the output as PASS/FAIL.
	 *
	 * You likely do not have to change this part of the framework. We are
	 * considering this complete and want your team to focus more on the
	 * SecurityTests.
	 */
	public void executeBasicTests() {
		StdoutWrapper.println("Running basic tests...");

		// iterate through the lists of tests and execute each one
		for (Test test : this.tests) {

			// instrument the code to code coverage metrics, execute the test
			// with given parameters, then
			// show the output
			Output output = instrumentAndExecuteCode(test.getParameters().toArray());
			printBasicTestOutput(output);
			ai.analyze(output);

			boolean outMatch = false, errMatch = false;
			try {
				outMatch = output.getStdOutString().matches(test.getStdOutExpectedResultRegex());
				errMatch = output.getStdErrString().matches(test.getStdErrExpectedResultRegex());
			} catch (Exception e) {

			}
			// determine the result of the test based on expected output/error
			// regex
			if (outMatch && errMatch) {
				StdoutWrapper.println("basic test result: PASS");
				passed++;
			} else {
				StdoutWrapper.println("basic test result: FAIL ");
				failed++;

				// since we have a failed basic test, show the expectation for
				// the stdout
				if (!outMatch) {
					StdoutWrapper.println("\t ->stdout: " + output.getStdOutString());
					StdoutWrapper.println(
							"\t ->did not match expected stdout regex: " + test.getStdOutExpectedResultRegex());
				}

				// since we have a failed basic test, show the expectation for
				// the stderr
				if (!errMatch) {
					StdoutWrapper.println("\t ->stderr: " + output.getStdErrString());
					StdoutWrapper.println(
							"\t ->did not match expected stderr regex: " + test.getStdErrExpectedResultRegex());
				}
			}
			total++;
			StdoutWrapper.println(HORIZONTAL_LINE);
		}
		// print the basic test results and the code coverage associated with
		// the basic tests
		double percentCovered = generateSummaryCodeCoverageResults();
		StdoutWrapper.println("basic test results: " + total + " total, " + passed + " pass, " + failed + " fail, "
				+ percentCovered + " percent covered");
		StdoutWrapper.println(HORIZONTAL_LINE);
	}

	/**
	 * A DFS driven by {@link ParameterFactory#getNext(List)}.
	 *
	 * Now we only add a path if getNext(path) return empty. Another strategy is
	 * to add every path to generate more parameter inputs. But that strategy is
	 * unrelated to the test coverage bottleneck. So that strategy is not tried.
	 */
	void gen(List<String> built, List<List<String>> acc, Set<List<String>> visited) {
		// Skip already visited path
		if (visited.contains(built)) {
			acc.add(new ArrayList<>(built));
			return;
		}
		visited.add(built);
		List<Parameter> params = this.parameterFactory.getNext(built);
		if (params.isEmpty()) {
			acc.add(new ArrayList<>(built));
			return;
		}
		dfs(built, acc, params, 0, visited);
	}

	/**
	 * Recursively building a params list to traverse through
	 * 
	 * @param built
	 *            existing parameters in the test case.
	 * @param acc
	 *            accumulator that will store all the possible combinations we
	 *            are going to test.
	 * @param params
	 *            parameter information.
	 * @param idx
	 *            index of current parameter to generate.
	 * @param visited
	 *            combinations already checked.
	 */
	@SuppressWarnings("rawtypes")
	private void dfs(List<String> built, List<List<String>> acc, List<Parameter> params, int idx,
			Set<List<String>> visited) {
		if (idx >= params.size()) {
			gen(built, acc, visited);
			return;
		}
		Parameter p = params.get(idx);
		// Optional parameter causes a choice.
		if (p.isOptional()) {
			int oldSize = built.size();
			dfs(built, acc, params, idx + 1, visited);
			built = built.subList(0, oldSize);
		}
		if (p instanceof EnumerationParameter) {
			List<Generator> gs = p.generators();
			for (int i = 0; i < gs.size(); i++) {
				int oldSize = built.size();
				String next = gs.get(i).nextFormatted();
				built.add(next);
				dfs(built, acc, params, idx + 1, visited);
				built = built.subList(0, oldSize);
			}
		} else {
			String next = p.generators().get(0).nextFormatted();
			built.add(next);
			dfs(built, acc, params, idx + 1, visited);
		}
	}

	/**
	 * This is the half of the framework that IDT has not completed. We want you
	 * to implement your exploratory security vulnerability testing here.
	 *
	 * In an effort to demonstrate some of the features of the framework that
	 * you can already utilize, we have provided some example code in the
	 * method. The examples only demonstrate how to use existing functionality.
	 */
	public void executeSecurityTests() {
		long startTs = System.currentTimeMillis();
		StdoutWrapper.println("Running security tests...");
		if (timeGoal > 0) {
			Thread t = new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(timeGoal * 1000);
						timeUp = true;
					} catch (InterruptedException e) {
						throw new RuntimeException();
					}
				}
			};
			t.start();
		}

		int killed = 0;
		Set<String> errs = new HashSet<>();
		outerLoop: while ((timeGoal > 0 && !timeUp) || total < bbTests) {
			List<List<String>> acc = new LinkedList<>();
			gen(new LinkedList<>(), acc, new HashSet<>());
			for (int i = 0; i < acc.size(); i++) {
				List<String> params = acc.get(i);
				ai.adapt(acc, params);
				StdoutWrapper.printf("Run %d with params %s\n", total + 1, params);
				Object[] array = params.toArray();
				Output output = instrumentAndExecuteCode(array);
				if (output == null) {
					killed++;
				} else {
					Set<String> oneRunErrs = output.getErrors();
					if (oneRunErrs.size() > 0) {
						failed++;
						errs.addAll(oneRunErrs);
					}
					total++;
					if (!((timeGoal > 0 && !timeUp) || total < bbTests))
						break outerLoop;
					printBasicTestOutput(output);
					ai.analyze(output);
				}
			}
		}
		long endTs = System.currentTimeMillis();
		String durationStr = DurationFormatUtils.formatDurationHMS(endTs - startTs);
		StdoutWrapper.printf("IDT has run security tests for %s.\n", durationStr);
		double percentCovered = generateSummaryCodeCoverageResults();
		YamlReport yamlReport = new YamlReport(total, failed, percentCovered, errs);
		yamlReport.write();
		if (killed > 0) {
			log.error("{} execution processes have been killed", killed);
		}
	}

	//////////////////////////////////////////
	// PRIVATE METHODS
	//////////////////////////////////////////

	/**
	 * This method will instrument and execute the jar under test with the
	 * supplied parameters. This method should be used for both basic tests and
	 * security tests.
	 *
	 * An assumption is made in this method that the word java is recognized on
	 * the command line because the user has already set the appropriate
	 * environment variable path.
	 *
	 * @param parameters
	 *            - array of Objects that represents the parameter values to use
	 *            for this execution of the jar under test
	 *
	 * @return Output representation of the standard out and standard error
	 *         associated with the run. A <tt>null</null> return value means
	 *         that a long running execution process has been killed.
	 */
	public Output instrumentAndExecuteCode(Object[] parameters) {
		Output output = null;
		String command = createCommand(parameters);

		// we are building up a command line statement that will use java -jar
		// to execute the jar
		// and uses jacoco to instrument that jar and collect code coverage
		// metrics
		try {
			// show the user the command to run and prepare the process using
			// the command
			StdoutWrapper.println("command to run: " + command);
			final Process process = Runtime.getRuntime().exec(command);

			// prepare the stream needed to capture standard output
			InputStream isOut = process.getInputStream();
			InputStreamReader isrOut = new InputStreamReader(isOut);
			BufferedReader brOut = new BufferedReader(isrOut);
			StringBuffer stdOutBuff = new StringBuffer();

			// prepare the stream needed to capture standard error
			InputStream isErr = process.getErrorStream();
			InputStreamReader isrErr = new InputStreamReader(isErr);
			BufferedReader brErr = new BufferedReader(isrErr);
			StringBuffer stdErrBuff = new StringBuffer();

			Thread processDestroyer = new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(ONE_RUN_SECONDS * 1000);
						log.error("'{}' has been running more than {} seconds. Terminating it...", command,
								ONE_RUN_SECONDS);
						if (process.isAlive())
							process.destroyForcibly();
						log.error("The command has been terminated");
					} catch (InterruptedException ie) {
						// interrupted when the process has been killed by
						// destroyForcibly after the below while
						// loop.
					}
				}
			};
			processDestroyer.start();

			String line;
			boolean outDone = false;
			boolean errDone = false;

			while (!outDone || !errDone) {
				line = brOut.readLine();
				if (line == null) {
					outDone = true;
				} else {
					stdOutBuff.append(line);
				}

				line = brErr.readLine();
				if (line == null) {
					errDone = true;
				} else {
					// Suppress unrelated JavaLaunchHelper error message on
					// macOS,
					// as reported at
					// http://contest.idtus.com/contest/highschool/?q=node/81
					if (!line.matches("objc\\[\\d*\\]: Class JavaLaunchHelper is implemented in both.*"))
						stdErrBuff.append(line);
				}
			}

			process.destroyForcibly();
			processDestroyer.interrupt();
			output = new Output(stdOutBuff.toString(), stdErrBuff.toString());
		} catch (IOException e) {
			String msg = "ERROR: IOException has prevented execution of the command: '" + command + "'";
			log.error(msg, e);
		}
		return output;
	}

	/**
	 * Method used to print the basic test output (std out/err)
	 * 
	 * @param output
	 *            - Output object containing std out/err to print
	 */
	private void printBasicTestOutput(Output output) {
		StdoutWrapper.println("stdout of execution: " + output.getStdOutString());
		StdoutWrapper.println("stderr of execution: " + output.getStdErrString());
	}

	/**
	 * Method used to print raw code coverage stats including hits/probes
	 * 
	 * @throws IOException
	 */
	private void printRawCoverageStats() {
		System.out.printf("exec file: %s%n", this.jacocoOutputFilePath);
		System.out.println("CLASS ID         HITS/PROBES   CLASS NAME");

		try {
			File executionDataFile = new File(this.jacocoOutputFilePath);
			final FileInputStream in = new FileInputStream(executionDataFile);
			final ExecutionDataReader reader = new ExecutionDataReader(in);
			reader.setSessionInfoVisitor(new ISessionInfoVisitor() {

				public void visitSessionInfo(final SessionInfo info) {
					System.out.printf("Session \"%s\": %s - %s%n", info.getId(), new Date(info.getStartTimeStamp()),
							new Date(info.getDumpTimeStamp()));
				}
			});
			reader.setExecutionDataVisitor(new IExecutionDataVisitor() {

				public void visitClassExecution(final ExecutionData data) {
					System.out.printf("%016x  %3d of %3d   %s%n", Long.valueOf(data.getId()),
							Integer.valueOf(getHitCount(data.getProbes())), Integer.valueOf(data.getProbes().length),
							data.getName());
				}
			});
			reader.read();
			in.close();
		} catch (IOException e) {
			System.out.println(
					"Unable to display raw coverage stats due to IOException related to " + this.jacocoOutputFilePath);
		}
		System.out.println();
	}

	/**
	 * Method used to get hit count from the code coverage metrics
	 * 
	 * @param data
	 *            - boolean array of coverage data where true indicates hits
	 * @return int representation of count of total hits from supplied data
	 */
	private int getHitCount(final boolean[] data) {
		int count = 0;
		for (final boolean hit : data) {
			if (hit) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Method for generating code coverage metrics including instructions,
	 * branches, lines, methods and complexity.
	 *
	 * @return double representation of the percentage of code covered during
	 *         testing
	 */
	private double generateSummaryCodeCoverageResults() {
		double percentCovered = 0.0;
		long total = 0;
		long covered = 0;
		try {
			// creating a new file for output in the jacoco output directory
			// (one of the application arguments)
			File executionDataFile = new File(this.jacocoOutputFilePath);
			ExecFileLoader execFileLoader = new ExecFileLoader();
			execFileLoader.load(executionDataFile);

			// use CoverageBuilder and Analyzer to assess code coverage from
			// jacoco output file
			final CoverageBuilder coverageBuilder = new CoverageBuilder();
			final Analyzer analyzer = new Analyzer(execFileLoader.getExecutionDataStore(), coverageBuilder);

			// analyzeAll is the way to go to analyze all classes inside a
			// container (jar or zip or directory)
			analyzer.analyzeAll(new File(this.jarToTestPath));

			for (final IClassCoverage cc : coverageBuilder.getClasses()) {

				// report code coverage from all classes that are not the
				// TestBounds class within the jar
				if (cc.getName().endsWith("TestBounds") == false) {
					total += cc.getInstructionCounter().getTotalCount();
					total += cc.getBranchCounter().getTotalCount();
					total += cc.getLineCounter().getTotalCount();
					total += cc.getMethodCounter().getTotalCount();
					total += cc.getComplexityCounter().getTotalCount();

					covered += cc.getInstructionCounter().getCoveredCount();
					covered += cc.getBranchCounter().getCoveredCount();
					covered += cc.getLineCounter().getCoveredCount();
					covered += cc.getMethodCounter().getCoveredCount();
					covered += cc.getComplexityCounter().getCoveredCount();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		percentCovered = ((double) covered / (double) total) * 100.0;
		return percentCovered;
	}

	/**
	 * This method shows an example of how to generate code coverage metrics
	 * from Jacoco
	 *
	 * @return String representing code coverage results
	 */
	private String generateDetailedCodeCoverageResults() {
		String executionResults = "";
		try {
			File executionDataFile = new File(this.jacocoOutputFilePath);
			ExecFileLoader execFileLoader = new ExecFileLoader();
			execFileLoader.load(executionDataFile);

			final CoverageBuilder coverageBuilder = new CoverageBuilder();
			final Analyzer analyzer = new Analyzer(execFileLoader.getExecutionDataStore(), coverageBuilder);

			analyzer.analyzeAll(new File(this.jarToTestPath));

			for (final IClassCoverage cc : coverageBuilder.getClasses()) {
				executionResults += "Coverage of class " + cc.getName() + ":\n";
				executionResults += getMetricResultString("instructions", cc.getInstructionCounter());
				executionResults += getMetricResultString("branches", cc.getBranchCounter());
				executionResults += getMetricResultString("lines", cc.getLineCounter());
				executionResults += getMetricResultString("methods", cc.getMethodCounter());
				executionResults += getMetricResultString("complexity", cc.getComplexityCounter());

				// adding this to a string is a little impractical with the size
				// of some of the files,
				// so we are commenting it out, but it shows that you can get
				// the coverage status of each line
				// if you wanted to add debug argument to display this level of
				// detail at command line level....
				//
				// for (int i = cc.getFirstLine(); i <= cc.getLastLine(); i++) {
				// executionResults += "Line " + Integer.valueOf(i) + ": " +
				// getStatusString(cc.getLine(i).getStatus()) + "\n";
				// }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return executionResults;
	}

	/**
	 * Method to translate the Jacoco line coverage status integers to Strings.
	 *
	 * @param status
	 *            - integer representation of line coverage status provided by
	 *            Jacoco
	 * @return String representation of line coverage status (not covered,
	 *         partially covered, fully covered)
	 */
	@SuppressWarnings("unused")
	private String getStatusString(final int status) {
		switch (status) {
		case ICounter.NOT_COVERED:
			return "not covered";
		case ICounter.PARTLY_COVERED:
			return "partially covered";
		case ICounter.FULLY_COVERED:
			return "fully covered";
		}
		return "";
	}

	/**
	 * Method to translate the counter data and units into a human readable
	 * metric result String
	 *
	 * @param unit
	 * @param counter
	 * @return
	 */
	private String getMetricResultString(final String unit, final ICounter counter) {
		final Integer missedCount = Integer.valueOf(counter.getMissedCount());
		final Integer totalCount = Integer.valueOf(counter.getTotalCount());
		return missedCount.toString() + " of " + totalCount.toString() + " " + unit + " missed\n";
	}

	/**
	 * This method is not meant to be part of the final framework. It was
	 * included to demonstrate three different ways to tap into the code
	 * coverage results/metrics using jacoco.
	 *
	 * This method is deprecated and will be removed from the final product
	 * after your team completes development. Please do not add additional
	 * dependencies to this method.
	 */
	@Deprecated
	private void showCodeCoverageResultsExample() {

		// Below is the first example of how to tap into code coverage metrics
		double result = generateSummaryCodeCoverageResults();
		System.out.println("\n");
		System.out.println("percent covered: " + result);

		// Below is the second example of how to tap into code coverage metrics
		System.out.println("\n");
		printRawCoverageStats();

		// Below is the third example of how to tap into code coverage metrics
		System.out.println("\n");
		System.out.println(generateDetailedCodeCoverageResults());
	}

	// For unit test.
	public ParameterFactory getParameterFactory() {
		return parameterFactory;
	}

	private String createCommand(Object[] parameters) {
		String command = "java";
		command += " -javaagent:" + this.jacocoAgentJarPath + "=destfile=" + this.jacocoOutputFilePath;
		command += " -jar " + this.jarToTestPath;
		for (Object o : parameters) {
			command += " " + o.toString();
		}
		return command;
	}

	private void extractExternalTests() {
		JsonParser parser = new JsonParser(getJsonFileName());
		Test[] externalTests = parser.parse();

		if (externalTests != null && tests != null) {
			tests.addAll(Arrays.asList(externalTests));
		}
	}

	private String getJsonFileName() {
		int begin = jarToTestPath.lastIndexOf(File.separator);
		int end = jarToTestPath.indexOf(".jar");
		return jarToTestPath.substring(begin + 1, end) + ".json";
	}

}
