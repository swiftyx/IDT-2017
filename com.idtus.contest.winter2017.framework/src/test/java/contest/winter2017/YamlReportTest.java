package contest.winter2017;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class YamlReportTest {

	@Test
	public void testWrite() {
		Set<String> errors = new HashSet<>();
		errors.add("error1-line1\nerror1-line2\r\nerror1-line3");
		errors.add("erro2");
		YamlReport report = new YamlReport(3, 2, 0.78, errors);
		report.write();
	}

}
