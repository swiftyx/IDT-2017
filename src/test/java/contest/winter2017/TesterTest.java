package contest.winter2017;

import static contest.winter2017.Config.JAR_CommandLineEncryption;
import static contest.winter2017.Config.JAR_LeetConverter;
import static contest.winter2017.Config.JAR_RegexPatternMatch;
import static contest.winter2017.Config.JAR_TesterTypeCheck;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import com.google.common.truth.Truth;

public class TesterTest {

	@Test
	public void testLongRunningProcess() {
		String input = "--encrypt input=bad password=bad keyObtentionIterations=" + Integer.MAX_VALUE;
		Tester t = create(JAR_CommandLineEncryption);
		t.instrumentAndExecuteCode(input.split(" "));
	}

	@Test
	public void test10sProcess() {
		String input = "--Integer 1";
		Tester t = create(JAR_TesterTypeCheck);
		t.instrumentAndExecuteCode(input.split(" "));
	}

	@Test
	public void testRegexPatternMatchSecurityTests() {
		create(JAR_RegexPatternMatch, 100).executeSecurityTests();
	}

	@Test
	public void testLeetConverterSecurityTests() {
		create(JAR_LeetConverter, 20).executeSecurityTests();
	}

	@Test
	public void testTesterTypeCheckSecurityTests() {
		create(JAR_TesterTypeCheck, 50).executeSecurityTests();
	}

	@Test
	public void testCommandLineEncryptionSecurityTests() {
		create(JAR_CommandLineEncryption, 100).executeSecurityTests();
	}

	@Test
	public void testRegexPatternMatchBasicTests() {
		create(JAR_RegexPatternMatch, 100).executeBasicTests();
	}

	@Test
	public void testLeetConverterBasicTests() {
		create(JAR_LeetConverter, 20).executeBasicTests();
	}

	@Test
	public void testTesterTypeCheckBasicTests() {
		create(JAR_TesterTypeCheck, 50).executeBasicTests();
	}

	@Test
	public void testCommandLineEncryptionBasicTests() {
		create(JAR_CommandLineEncryption, 100).executeBasicTests();
	}

	/**
	 * Prints paramter meta data for test jars.
	 */
	@Test
	public void testCreate() {
		create(JAR_RegexPatternMatch);
		create(JAR_LeetConverter);
		create(JAR_TesterTypeCheck);
		create(JAR_CommandLineEncryption);
	}

	@Test
	public void testGenTimes() {
		verifyGen(JAR_RegexPatternMatch, 10);
		verifyGen(JAR_LeetConverter, 10);
		verifyGen(JAR_TesterTypeCheck, 10);
		verifyGen(JAR_CommandLineEncryption, 10);
	}

	@Test
	public void testGenOnce() {
		verifyGen(JAR_RegexPatternMatch, 1, 1);
		verifyGen(JAR_LeetConverter, 1, 3);
		verifyGen(JAR_TesterTypeCheck, 1, 6);
		verifyGen(JAR_CommandLineEncryption, 1, 35);
	}

	/**
	 * If an whitespace string like " " is added as parameter, endless recursion
	 * might occur.
	 */
	@Test
	public void endlessRecursion() {
		Tester t1 = create(JAR_CommandLineEncryption);
		ParameterFactory pf = t1.getParameterFactory();
		List<String> strs = new ArrayList<>();
		strs.add("--encrypt");
		System.out.println(pf.getNext(strs)); // First-time
		strs.add(" ");
		System.out.println(pf.getNext(strs)); // Still the same as first-time
	}

	private void verifyGen(String name, int count) {
		verifyGen(name, count, -1);
	}

	private void verifyGen(String name, int count, int expectedInputCount) {
		System.out.println("## " + name);
		Tester t = create(name);
		for (int i = 0; i < count; i++) {
			List<List<String>> acc = new ArrayList<>();
			t.gen(new ArrayList<>(), acc, new HashSet<>());
			int actualInputCount = acc.size();
			if (expectedInputCount > 0)
				Truth.assertThat(actualInputCount).isEqualTo(expectedInputCount);
			System.out.printf("%d inputs: %s\n", actualInputCount, acc);
		}
	}

	private Tester create(String name, int bbTests) {
		System.out.printf("## %s\n", name);
		Tester t = new Tester();
		t.init(Config.i().getJarsDir() + "/" + name + ".jar", Config.i().getJacocoOutputPath(),
				Config.i().getJacocoAgentJarPath(), bbTests, -1);
		return t;
	}

	private Tester create(String name) {
		return create(name, 100);
	}
}
