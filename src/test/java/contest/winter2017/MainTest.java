package contest.winter2017;

import static contest.winter2017.Config.JAR_CommandLineEncryption;
import static contest.winter2017.Config.JAR_LeetConverter;
import static contest.winter2017.Config.JAR_RegexPatternMatch;
import static contest.winter2017.Config.JAR_TesterTypeCheck;

import org.junit.Test;

public class MainTest {
	@Test
	public void testRegexPatternMatch() {
		runMain(JAR_RegexPatternMatch, 100, 5);
	}

	@Test
	public void testLeetConverter() {
		runMain(JAR_LeetConverter, 20, 5);
	}

	@Test
	public void testTesterTypeCheck() {
		runMain(JAR_TesterTypeCheck, 50, 5);
	}

	@Test
	public void testCommandLineEncryption() {
		runMain(JAR_CommandLineEncryption, 100, 5);
	}

	private void runMain(String name, int bbTests, int timeGoal) {
		Main.runMain(Config.i().getJacocoAgentJarPath(), Config.i().getJacocoOutputPath(),
				Config.i().getJarsDir() + "/" + name + ".jar", Integer.toString(bbTests), Integer.toString(timeGoal),
				false);
	}
}
