package contest.winter2017.parameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

/**
 * Generator for normal String generation.
 */
public class StringGenerator extends Generator<String> {
	/** Temporary index tracker. */
	private int randomIdx = 0;
	/** Randomness generator. */
	private Random r = new Random();

	/** Argument label. */
	private String label;
	/** May have value as part of argument. */
	private boolean hasArg;

	/** Constructs a new StringGenerator that generates any random Strings. */
	private StringGenerator() {
		this(null, true);
	}

	/**
	 * Constructs a new StringGenerator with given label.
	 * 
	 * @param label
	 *            the label in front of the option. For example, `-add 1`,
	 *            `-add` is the option.
	 * @param hasArg
	 *            if contains actual value as argument
	 */
	private StringGenerator(String label, boolean hasArg) {
		this.label = label == null ? null : label + " ";
		this.hasArg = hasArg;
		super.init();
	}

	/**
	 * Constructs a new StringGenerator that generates any random Strings.
	 * 
	 * @return new StringGenerator object.
	 */
	public static StringGenerator makeRandom() {
		return new StringGenerator();
	}

	/**
	 * Constructs a new StringGenerator with given label.
	 * 
	 * @param option
	 *            the label in front of the option. For example, `-add 1`,
	 *            `-add` is the option. It shall not be blank.
	 * @param hasArg
	 *            if contains actual value as argument
	 * @return new StringGenerator object.
	 */
	public static StringGenerator makeWithConstant(String option, boolean hasArg) {
		Preconditions.checkArgument(StringUtils.isNotBlank(option));
		return new StringGenerator(option, hasArg);
	}

	@Override
	public String nextRandom() {
		if (label == null || hasArg) {
			String base = label == null ? "" : label;
			if (randomIdx++ < 10) {
				return base + UUID.randomUUID().toString();
			}
			// FIXME: If string is too long, IOExcpeiton will be thrown when
			// invoking Process.exec.
			int len = r.nextInt(51);
			String random = RandomStringUtils.random(len);
			return base + random;
		}
		return label;
	}

	/** List of fixed parameters. */
	private static List<String> fixed;

	@Override
	public Object defaultList() {
		if (fixed == null) {
			fixed = new ArrayList<>(Arrays.asList(new String[] { "", "one", "second", "good", "bad", "=", "fixed-str",
					"1x", "北方", "MD2", "MD5", "SHA", "SHA-224", "SHA-256", "SHA-384", "SHA-512",
					"PBEWITHHMACSHA1ANDAES_128", "PBEWITHHMACSHA1ANDAES_256", "PBEWITHHMACSHA224ANDAES_128",
					"PBEWITHHMACSHA224ANDAES_256", "PBEWITHHMACSHA256ANDAES_128", "PBEWITHHMACSHA256ANDAES_256",
					"PBEWITHHMACSHA384ANDAES_128", "PBEWITHHMACSHA384ANDAES_256", "PBEWITHHMACSHA512ANDAES_128",
					"PBEWITHHMACSHA512ANDAES_256", "PBEWITHMD5ANDDES", "PBEWITHMD5ANDTRIPLEDES", "PBEWITHSHA1ANDDESEDE",
					"PBEWITHSHA1ANDRC2_128", "PBEWITHSHA1ANDRC2_40", "PBEWITHSHA1ANDRC4_128", "PBEWITHSHA1ANDRC4_40",
					"true", "false", "base64", "hexadecimal", "org.jasypt.salt.ByteArrayFixedSaltGenerator",
					"org.jasypt.salt.RandomSaltGenerator", "org.jasypt.salt.StringFixedSaltGenerator",
					"org.jasypt.salt.ZeroSaltGenerator", "org.jasypt.salt.FixedByteArraySaltGenerator",
					"org.jasypt.salt.FixedStringSaltGenerator", "(", "[", "[a", "{", "}", "%%%", "xx}", "x|", "X{3,1}",
					"X{-3,1}", "X{-3}" }));
			fixed.addAll(unicodes());
			fixed.addAll(RegExprs.regExprs());
		}
		if (hasArg) {
			if (label == null)
				return fixed;
			return fixed.stream().map(val -> label + val).collect(Collectors.toList());
		}
		return label;
	}

	/** @return predefined set of Unicode characters to test. */
	public static List<String> unicodes() {
		List<String> list = new ArrayList<>();
		// Basic Multilingual Plane characters
		list.add("\u0024");
		list.add("\u20AC");
		// Supplementary character
		list.add("\uD801\uDC37");
		list.add("\uD852\uDF62");
		return list;
	}
}
