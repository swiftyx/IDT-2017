package contest.winter2017;

import java.util.Set;

public class YamlReport {
	private int total;
	private int passed;
	private int failed;
	private double coverage;
	private Set<String> errors;

	public YamlReport(int total, int failed, double coverage, Set<String> errors) {
		this.total = total;
		this.passed = total - failed;
		this.failed = failed;
		this.coverage = coverage;
		this.errors = errors;
	}

	public void write() {
		StdoutWrapper.printYamlf("Total predefined tests run: %d\n", total);
		StdoutWrapper.printYamlf("Number of predefined tests that passed: %d\n", passed);
		StdoutWrapper.printYamlf("Number of predefined tests that failed: %d\n", failed);
		StdoutWrapper.printYamlf("Total code coverage percentage: %f\n", coverage);
		int n = errors.size();
		StdoutWrapper.printYamlf("Unique error count: %d\n", n);
		if (n > 0) {
			StdoutWrapper.printYamlf("Errors seen:\n");
			for (String e : errors) {
				e = e.replaceAll("\n|\r\n", "\n    ");
				StdoutWrapper.printYamlf("  - %s\n", e);
			}
		}
	}
}
