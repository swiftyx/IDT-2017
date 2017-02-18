package contest.winter2017.parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;

/**
 * Generator for generating strings with placeholder(s) in it.
 */
public class FormattedStringGenerator extends Generator<String> {
	/** Placeholder format. */
	public static final String REPLACE_PATTERN = "<<REPLACE_ME_(STRING|INT)>>";
	/** Regex that format shall match. */
	public static final String MATCH_PATTERN = ".*" + REPLACE_PATTERN + ".*";

	/** Placeholder for String. */
	public static final String REPLACE_STRING = "<<REPLACE_ME_STRING>>";
	/** Placeholder for int. */
	public static final String REPLACE_INT = "<<REPLACE_ME_INT>>";

	/** Pattern to replace if the parameter is formatted. */
	private Pattern replaceMePattern = null;

	/** Format string with the <<REPLACE_ME_...>> in it. */
	private String format;

	/** Generators that generates supply random parameters. */
	@SuppressWarnings("rawtypes")
	private List<Generator> gs;

	/**
	 * Constructs a new FormattedStringGenerator with given format. Will include
	 * defaultList.
	 * 
	 * @param format
	 *            string with placeholder(s).
	 * @return new FormattedStringGenerator object.
	 */
	public static FormattedStringGenerator make(String format) {
		return make(format, false);
	}

	/**
	 * Constructs a new FormattedStringGenerator with given format.
	 * 
	 * @param format
	 *            string with placeholder(s).
	 * @param includeDefaultValues
	 *            if include defaultList or not.
	 * @return new FormattedStringGenerator object.
	 */
	public static FormattedStringGenerator make(String format, boolean includeDefaultValues) {
		FormattedStringGenerator g = new FormattedStringGenerator(format);
		if (includeDefaultValues)
			g.init();
		return g;
	}

	/**
	 * Constructs a new FormattedStringGenerator, and initailize generators for
	 * parameters.
	 * 
	 * @param format
	 *            string with placeholder(s).
	 */
	@SuppressWarnings("rawtypes")
	private FormattedStringGenerator(String format) {
		Preconditions.checkNotNull(format, "format can't be null");
		Preconditions.checkArgument(format.matches(MATCH_PATTERN),
				"format '" + format + "' does not match regular expression '" + MATCH_PATTERN + "'");
		this.format = format;
		this.replaceMePattern = Pattern.compile(REPLACE_PATTERN);

		List<Class> varTypes = new ArrayList<Class>();
		Matcher replaceMeMatcher = replaceMePattern.matcher(format);
		while (replaceMeMatcher.find()) {
			switch (replaceMeMatcher.group()) {
			case REPLACE_STRING: {
				varTypes.add(String.class);
				break;
			}
			case REPLACE_INT: {
				varTypes.add(Integer.class);
				break;
			}
			default:
				break;
			}
		}

		gs = new ArrayList<>();
		for (Class type : varTypes) {
			Generator g;
			if (type == Integer.class)
				g = IntegerGenerator.make();
			else if (type == String.class)
				g = StringGenerator.makeRandom();
			else
				throw new IllegalArgumentException(
						type.getSimpleName() + " can't be used as a type of the placeholder in formatted parameter. ");
			gs.add(g);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String nextRandom() {
		List<String> values = new ArrayList<String>(gs.size());
		for (Generator g : gs) {
			String val = "";
			do {
				val = g.nextFormatted();
			} while (val.trim().isEmpty());
			values.add(val);
		}
		return getFormattedParameter(values);
	}

	/**
	 * Utility method to build a valid formatted parameter by replacing all of
	 * the <<REPLACE_ME_...>> in the format parameter string
	 * 
	 * @param formatVariableValues
	 *            List<String> containing the values that will replace the
	 *            format for <<REPLACE_ME_...>> placeholders of this formatted
	 *            parameter
	 * @return String containing the parameter with <<REPLACE_ME_...>>
	 *         placeholders replaced with the passed in values
	 */
	public String getFormattedParameter(List<String> formatVariableValues) {
		Matcher replaceMeMatcher = replaceMePattern.matcher(format);
		StringBuffer sb = new StringBuffer();
		for (String variable : formatVariableValues) {
			if (replaceMeMatcher.find()) {
				variable = Matcher.quoteReplacement(variable);
				replaceMeMatcher.appendReplacement(sb, variable);
			}
		}
		replaceMeMatcher.appendTail(sb);
		return sb.toString();
	}

	// FIXME: Use better default?
	@Override
	public Object defaultList() {
		List<String> list = new ArrayList<String>();
		list.add(format);
		list.add(format.substring(1));
		list.add(format.substring(0, format.length() - 1));
		list.add("-" + format);
		list.add(format + "-");
		return list;
	}
}
