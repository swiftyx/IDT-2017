package contest.winter2017.parameter;

/**
 * Binary accessing iterator over integers.
 */
class IntegerRangeIterator extends BinaryAccessingIterator<Integer> {
	/** Min value */
	private final int min;
	/** Max value */
	private final int max;
	/** Number of elements */
	private final long count;

	/** Constructs an iterator over all integer */
	public IntegerRangeIterator() {
		this(Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * Constructs an iterator over [min, max].
	 * 
	 * @param min
	 *            min value.
	 * @param max
	 *            max value.
	 */
	private IntegerRangeIterator(int min, int max) {
		this.min = min;
		this.max = max;
		this.count = this.max - (long) this.min + 1;
	}

	/**
	 * Constructs an iterator over [min, max].
	 * 
	 * @param min
	 *            min value.
	 * @param max
	 *            max value.
	 * @return new IntegerRangeIterator object.
	 */
	public static IntegerRangeIterator make(int min, int max) {
		if (min > max) {
			return null;
		}
		return new IntegerRangeIterator(min, max);
	}

	@Override
	Integer getMin() {
		return min;
	}

	@Override
	Integer getMax() {
		return max;
	}

	@Override
	Integer getMid() {
		return (int) (min + count / 2);
	}

	@Override
	public Object count() {
		return count;
	}

	@Override
	BinaryAccessingIterator<Integer> leftSubIterator() {
		return make(min + 1, getMid() - 1);
	}

	@Override
	BinaryAccessingIterator<Integer> rightSubIterator() {
		return make(getMid() + 1, max - 1);
	}
}
