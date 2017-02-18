package contest.winter2017.parameter;

/**
 * CharacterSet that consists of characters bounded by a/several range(s).
 */
public class CharacterSet {
	/** CharacterSet of 0-9, from 48 to 57. */
	public static final CharacterSet DIGIT = new CharacterSet(48, 57);
	/** CharacterSet of A-Z, from 65 to 90. */
	public static final CharacterSet UPPER_CASE = new CharacterSet(65, 90);
	/** CharacterSet of a-z, from 97 to 122. */
	public static final CharacterSet LOWER_CASE = new CharacterSet(97, 122);
	/** CharacterSet of all visible ASCII characters, from 32 to 126. */
	public static final CharacterSet ASCII_VISIBLE = new CharacterSet(33, 126);
	/** CharacterSet of all ASCII characters, from 0 to 127. */
	public static final CharacterSet ASCII = new CharacterSet(127);
	/** CharacterSet of all characters. from 0 to 65535. */
	public static final CharacterSet UTF8 = new CharacterSet(65535);

	/** Minimum Unicode value of characters in the this. */
	private final int min;
	/** Maximum Unicode value of characters in the this. */
	private final int max;

	/**
	 * Constructs a CharacterSet with characters from \\u0000 to \\u{max}.
	 * 
	 * @param max
	 *            maximum int value of the characters in the set.
	 */
	public CharacterSet(int max) {
		this(0, max);
	}

	/**
	 * Constructs a CharacterSet with characters from \\u{min} to \\u{max}.
	 * 
	 * @param min
	 *            minimum int value of the characters in the set.
	 * @param max
	 *            maximum int value of the characters in the set.
	 */
	public CharacterSet(int min, int max) {
		this.min = min;
		this.max = max;
	}

	/**
	 * Value of the character with the smallest Unicode index in the character
	 * set
	 * 
	 * @return int value of the smallest character
	 */
	public int getMin() {
		return min;
	}

	/**
	 * Value of the character with the biggest Unicode index in the character
	 * set
	 * 
	 * @return int value of the largest character
	 */
	public int getMax() {
		return max;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CharacterSet) {
			CharacterSet that = (CharacterSet) obj;
			return min == that.min && max == that.max;
		}
		return false;
	}

	@Override
	public String toString() {
		return (char) min + "..." + (char) max;
	}
}
