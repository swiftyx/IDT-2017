package contest.winter2017.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import contest.winter2017.Output;

public class PatternRecognizer {

	public Map<String, List<String>> map = new HashMap<>();

	public PatternRecognizer() {

	}

	public void analyze(Output output) {
		String out = output.getStdOutString();
		String err = output.getStdErrString();
		List<String> options = getEnumeration(out);
		if (options != null) {
			int idx = out.indexOf(options.get(0));
			try {
				String[] parts = out.substring(0, idx - 1).split("\\W+");
				String key = parts[parts.length - 1];
				if (!map.containsKey(key)) {
					map.put(key, options);
				}
			} catch (Exception e) {
				// Ignore
			}
		}
	}

	public void adapt(List<List<String>> acc, List<String> cur) {
		int size = cur.size();
		for (int i = 0; i < size; i++) {
			String arg = cur.get(i);
			for (Map.Entry<String, List<String>> item : map.entrySet()) {
				if (StringUtils.getJaroWinklerDistance(item.getKey().toLowerCase(), arg.toLowerCase()) > 0.6) {
					for (String newValue : item.getValue()) {
						List<String> copy = new ArrayList<>(size);
						copy.addAll(cur);
						String next = i + 1 == size ? "" : cur.get(i + 1);
						String[] result = reassign(newValue, arg, next).split(" ");
						copy.set(i, result[0]);
						if (result.length > 1) {
							if (i + 1 >= size) {
								copy.add(result[1]);
							} else {
								copy.set(i + 1, result[1]);
							}
						}
						if (acc.contains(copy)) {
							return;
						}
						acc.add(copy);
					}
				}
			}
		}
	}

	public static String reassign(String newValue, String arg, String next) {
		int idx = arg.indexOf("=");
		if (idx != -1) {
			return arg.substring(0, idx + 1) + newValue + " " + next;
		}
		idx = arg.indexOf(":");
		if (idx != -1) {
			return arg.substring(0, idx + 1) + newValue + " " + next;
		}
		return arg + " " + next;
	}

	private static Pattern REGEX_PATTERN = null;

	private static Pattern getRegexPattern() {
		if (REGEX_PATTERN == null) {
			REGEX_PATTERN = Pattern.compile("(reg(ular )?)(ex(p(ression)?)?)", Pattern.CASE_INSENSITIVE);
		}
		return REGEX_PATTERN;
	}

	public static boolean literallyContainsRegex(String src) {
		return getRegexPattern().matcher(src).find();
	}

	private static Pattern ENUM_PATTERN = null;

	private static Pattern getEnumPattern() {
		if (ENUM_PATTERN == null) {
			ENUM_PATTERN = Pattern.compile("[^\\[\\],\\|]+(?=[,\\|])");
		}
		return ENUM_PATTERN;
	}

	private static Pattern END_PATTERN = null;

	private static Pattern getEndPattern() {
		if (END_PATTERN == null) {
			END_PATTERN = Pattern.compile("[^\\s\\],\\|]+(?=[\\s\\[\\]]+)");
		}
		return END_PATTERN;
	}

	public static List<String> getEnumeration(String src) {
		src = " " + src + " ";
		Matcher matcher = getEnumPattern().matcher(src);
		List<String> matches = new ArrayList<>();
		int last = 0;
		while (matcher.find()) {
			matches.add(matcher.group().trim());
			last = matcher.end();
		}
		if (matches.size() > 0) {
			matcher = getEndPattern().matcher(src);
			if (matcher.find(last)) {
				matches.add(matcher.group());
			}
			return matches;
		}
		return null;
	}
}
