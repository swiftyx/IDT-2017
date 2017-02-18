package contest.winter2017.parameter;

/**
 * Iterator that iterates through a given character set/range of characters.
 */
class CharacterRangeIterator extends BinaryAccessingIterator<Character> {
	/**
	 * A integer range iterator. This class is implemented by converting integer
	 * to a character.
	 */
	private BinaryAccessingIterator<Integer> iterator;

	/** Constructs a CharacterRangeIterator with all available characters. */
	public CharacterRangeIterator() {
		this(CharacterSet.UTF8.getMin(), CharacterSet.UTF8.getMax());
	}

	/**
	 * Constructs a CharacterRangeIterator with all characters within bounds of
	 * the otherIterator.
	 * 
	 * @param otherIterator
	 *            A integer iterator that has bounds a set of character indices.
	 */
	public CharacterRangeIterator(BinaryAccessingIterator<Integer> otherIterator) {
		this(otherIterator.getMin(), otherIterator.getMax());
	}

	/**
	 * Constructs a CharacterRangeIterator with all characters within [min,
	 * max].
	 * 
	 * @param min
	 *            min Unicode value of the characters in set
	 * @param max
	 *            max Unicode value of the characters in set
	 */
	public CharacterRangeIterator(int min, int max) {
		iterator = IntegerRangeIterator.make(min, max);
	}

	@Override
	Character getMin() {
		return (char) iterator.getMin().intValue();
	}

	@Override
	Character getMax() {
		return (char) iterator.getMax().intValue();
	}

	@Override
	Character getMid() {
		return (char) iterator.getMid().intValue();
	}

	@Override
	public Object count() {
		return iterator.count();
	}

	@Override
	BinaryAccessingIterator<Character> leftSubIterator() {
		BinaryAccessingIterator<Integer> sub = iterator.leftSubIterator();
		return sub == null ? null : new CharacterRangeIterator(sub);
	}

	@Override
	BinaryAccessingIterator<Character> rightSubIterator() {
		BinaryAccessingIterator<Integer> sub = iterator.rightSubIterator();
		return sub == null ? null : new CharacterRangeIterator(sub);
	}
}
