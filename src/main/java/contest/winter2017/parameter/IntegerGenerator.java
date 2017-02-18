package contest.winter2017.parameter;

import java.util.concurrent.ThreadLocalRandom;

import contest.winter2017.util.NumberUtil;

/**
 * Generator to generate random integers.
 */
public class IntegerGenerator extends Generator<Integer> {
	/** Min value. */
	private int min;
	/** Max value. */
	private int max;

	/**
	 * Constructs an integer generator over all integer.
	 */
	private IntegerGenerator() {
		this(null, null);
	}

	/**
	 * Constructs an integer generator over [(int)minObj, (int)maxObj].
	 * 
	 * @param minObj
	 *            min value of all integers.
	 * @param maxObj
	 *            max value of all integers.
	 */
	private IntegerGenerator(Object minObj, Object maxObj) {
		this.min = NumberUtil.intFrom(minObj, Integer.MIN_VALUE);
		this.max = NumberUtil.intFrom(maxObj, Integer.MAX_VALUE);
		super.init(IntegerRangeIterator.make(this.min, this.max));
	}

	/**
	 * Constructs an integer generator over all integer.
	 * 
	 * @return new IntegerGenerator object.
	 */
	public static IntegerGenerator make() {
		return new IntegerGenerator();
	}

	/**
	 * Constructs an integer generator over [(int)minObj, (int)maxObj].
	 * 
	 * @param min
	 *            min value of all integers.
	 * @param max
	 *            max value of all integers.
	 * @return new IntegerGenerator object.
	 */
	public static IntegerGenerator makeByBound(Object min, Object max) {
		return new IntegerGenerator(min, max);
	}

	@Override
	public Object defaultList() {
		return new String[] { Integer.toString(0), Integer.toString(1), Integer.toString(Integer.MIN_VALUE),
				Integer.toString(Integer.MAX_VALUE), Integer.toHexString(0xF), Integer.toOctalString(10),
				Long.toString(Long.MIN_VALUE), Long.toString(Long.MAX_VALUE), "+", "-", "0x0", "0.1" };
	}

	@Override
	public String nextRandom() {
		/*
		 * Although here we use long, it's going to be an int. This is just
		 * avoiding overflow of Intger.MAX_VALUE.
		 */
		return Long.toString(ThreadLocalRandom.current().nextLong(min, max + (long) 1));
	}
}
