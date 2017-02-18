package contest.winter2017.parameter;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Generator that generates random character values.
 */
public class CharacterGenerator extends Generator<Character> {
	/** Constructs a generator over all visible ASCII characters. */
	private CharacterGenerator() {
		this(CharacterSet.ASCII_VISIBLE);
	}

	/**
	 * Constructs a generator over all characters within range [0, max].
	 *
	 * @param max
	 *            maximum Unicode value of all characters
	 */
	private CharacterGenerator(int max) {
		this(0, max);
	}

	/**
	 * Constructs a generator over all characters within range [min, max].
	 * 
	 * @param min
	 *            minimum Unicode value of all characters
	 * @param max
	 *            maximum Unicode value of all characters
	 */
	private CharacterGenerator(int min, int max) {
		this(new CharacterSet(min, max));
	}

	/**
	 * Constructs a generator over all characters in sets.
	 * 
	 * @param sets
	 *            array of CharacterSets to generate within.
	 */
	private CharacterGenerator(CharacterSet[] sets) {
		this(sets != null && sets.length > 0 ? sets[0] : null);
		if (sets != null && sets.length > 1) {
			for (int i = 1; i < sets.length; i++) {
				add(convert(sets[i]));
			}
		}
	}

	/**
	 * Constructs a generator over all characters in set.
	 * 
	 * @param set
	 *            CharacterSet to generate within.
	 */
	private CharacterGenerator(CharacterSet set) {
		super.init(convert(set));
	}

	/** @return CharacterGenerator over all visible ASCII characters. */
	public static CharacterGenerator make() {
		return new CharacterGenerator();
	}

	/**
	 * Constructs a generator over all characters within range [0, max].
	 *
	 * @param max
	 *            maximum Unicode value of all characters
	 * @return new CharacterGenerator object.
	 */
	public static CharacterGenerator makeWithMaxCodePoint(int max) {
		return new CharacterGenerator(max);
	}

	/**
	 * Constructs a generator over all characters within range [min, max].
	 * 
	 * @param min
	 *            minimum Unicode value of all characters
	 * @param max
	 *            maximum Unicode value of all characters
	 * @return new CharacterGenerator object.
	 */
	public static CharacterGenerator makeByBound(int min, int max) {
		return new CharacterGenerator(min, max);
	}

	/**
	 * Constructs a generator over all characters in sets.
	 * 
	 * @param sets
	 *            array of CharacterSets to generate within.
	 * @return new CharacterGenerator object.
	 */
	public static CharacterGenerator makeWithCharSets(CharacterSet[] sets) {
		return new CharacterGenerator(sets);
	}

	/**
	 * Constructs a generator over all characters in set.
	 * 
	 * @param set
	 *            CharacterSet to generate within.
	 * @return new CharacterGenerator object.
	 */
	public static CharacterGenerator makeWithCharSet(CharacterSet set) {
		return new CharacterGenerator(set);
	}

	/**
	 * Convert CharacterSet to iterable oterator.
	 * 
	 * @param set
	 *            A set of characters to iterate within.
	 * @return CharacterRangeIterator to iterate through.
	 */
	public static CharacterRangeIterator convert(CharacterSet set) {
		if (set != null) {
			return new CharacterRangeIterator(set.getMin(), set.getMax());
		}
		return convert(CharacterSet.ASCII_VISIBLE);
	}

	@Override
	public Object defaultList() {
		return null;
	}

	@Override
	public String nextRandom() {
		return "" + (char) ThreadLocalRandom.current().nextInt(65536);
	}
}
