package contest.winter2017;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;

/**
 * Class to hold output (std out/err) associated with a given test run
 * 
 * @author IDT
 */
public class Output {
	private static String ERR_PATTEN = ".*Exception.*";

	private Pattern pattern;

	/**
	 * String of the standard out associated with a given test run
	 */
	private String stdOutString = null;

	/**
	 * String of a standard error associated with a given test run
	 */
	private String stdErrString = null;

	/**
	 * Ctr for Output object
	 * 
	 * @param stdOutString
	 *            - std out string to hold
	 * @param stdErrString
	 *            - std err string to hold
	 */
	public Output(String stdOutString, String stdErrString) {
		Preconditions.checkNotNull(stdOutString);
		Preconditions.checkNotNull(stdErrString);
		this.stdOutString = stdOutString;
		this.stdErrString = stdErrString;
		pattern = Pattern.compile(ERR_PATTEN);
	}

	/**
	 * Getter for std out string
	 * 
	 * @return String representation of std out associated with a given test run
	 */
	public String getStdOutString() {
		return stdOutString;
	}

	/**
	 * Getter for std err string
	 * 
	 * @return String representation of std err associated with a given test run
	 */
	public String getStdErrString() {
		return stdErrString;
	}

	private boolean isErr(String string) {
		Matcher stdErrMatcher = pattern.matcher(string);
		return stdErrMatcher.find();
	}

	public Set<String> getErrors() {
		Set<String> errs = new HashSet<>();
		for (String s : new String[] { stdErrString, stdOutString })
			if (isErr(s))
				errs.add(s);
		return errs;
	}
}
