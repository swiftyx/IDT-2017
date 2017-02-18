package contest.winter2017.parameter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to handle regex list IO.
 */
public class RegExprs {
	/** Path to some regex. */
	private static final String PATH_NAME = "../parameter/validReg.txt";
	/** A logger. */
	private static final Logger log = LoggerFactory.getLogger(RegExprs.class);
	/** Lazy initializing regex list. */
	private static List<String> strs;

	/**
	 * Read and returns regular expressions from file.
	 * 
	 * @return List of regular expressions.
	 */
	public static List<String> regExprs() {
		if (strs == null) {
			try (Stream<String> stream = Files.lines(Paths.get(PATH_NAME))) {
				strs = stream.filter(line -> {
					return !StringUtils.isBlank(line) // Skip blank lines
							&& !line.matches("^\\s*#.*$"); // Skip comment lines
				}).collect(Collectors.toList());
				log.info("valid regular expressions: {}", strs);
			} catch (IOException ioex) {
				log.error("Error: IOException while reading regexs from file " + PATH_NAME, ioex);
				return new ArrayList<String>();
			}
		}
		return strs;
	}
}
