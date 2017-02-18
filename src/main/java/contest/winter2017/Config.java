package contest.winter2017;

import java.io.InputStream;

import org.yaml.snakeyaml.Yaml;

public class Config {
	public static final String JAR_TesterTypeCheck = "TesterTypeCheck";
	public static final String JAR_RegexPatternMatch = "RegexPatternMatch";
	public static final String JAR_LeetConverter = "LeetConverter";
	public static final String JAR_CommandLineEncryption = "CommandLineEncryption";

	private String jarsDir;
	private String jacocoOutputPath;
	private String jacocoAgentJarPath;
	private boolean saveStdout;

	public String getJarsDir() {
		return jarsDir;
	}

	public void setJarsDir(String jarsDir) {
		this.jarsDir = jarsDir;
	}

	public String getJacocoOutputPath() {
		return jacocoOutputPath;
	}

	public void setJacocoOutputPath(String jacocoOutputPath) {
		this.jacocoOutputPath = jacocoOutputPath;
	}

	public String getJacocoAgentJarPath() {
		return jacocoAgentJarPath;
	}

	public void setJacocoAgentJarPath(String jacocoAgentJarPath) {
		this.jacocoAgentJarPath = jacocoAgentJarPath;
	}

	public boolean isSaveStdout() {
		return saveStdout;
	}

	public void setSaveStdout(boolean saveStdout) {
		this.saveStdout = saveStdout;
	}

	@Override
	public String toString() {
		Yaml yaml = new Yaml();
		return yaml.dump(this);
	}

	private static Config INSTANCE;

	static {
		try (InputStream in = Config.class.getResourceAsStream("/config.yaml")) {
			Yaml yaml = new Yaml();
			INSTANCE = yaml.loadAs(in, Config.class);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static Config i() {
		return INSTANCE;
	}

}
