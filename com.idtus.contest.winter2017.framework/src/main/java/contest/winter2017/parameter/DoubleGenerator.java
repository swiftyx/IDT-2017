package contest.winter2017.parameter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.concurrent.ThreadLocalRandom;

import contest.winter2017.util.NumberUtil;

/**
 * Generator that generates double values.
 */
public class DoubleGenerator extends Generator<Double> {
	/** Random number generator */
	private ThreadLocalRandom lr = ThreadLocalRandom.current();

	/** Default min value */
	private double min = -Double.MAX_VALUE;
	/** Default max value */
	private double max = Double.MAX_VALUE;

	/**
	 * Constructs a DoubleGenerator over all doubles with interval of 0.1.
	 */
	private DoubleGenerator() {
		this(-Double.MAX_VALUE, Double.MAX_VALUE, 0.1);
	}

	/**
	 * Constructs a new DoubleGenerator over {min, min+step, ..., max-step,
	 * max}.
	 * 
	 * @param min
	 *            min value over all doubles
	 * @param max
	 *            max value over all doubles
	 * @param step
	 *            interval between two values
	 */
	private DoubleGenerator(double min, double max, double step) {
		setup(DoubleRangeIterator.makeByBound(min, max, step));
	}

	/**
	 * Constructs a new DoubleGenerator over [start, start+step*count].
	 * 
	 * @param start
	 *            min/max value over all doubles.
	 * @param step
	 *            interval between two values.
	 * @param count
	 *            number of elements.
	 */
	private DoubleGenerator(double start, double step, BigInteger count) {
		setup(DoubleRangeIterator.makeByCount(start, step, count));
	}

	/**
	 * Let the superclass setup predefined values, and get boundaries for this
	 * to generate random.
	 * 
	 * @param iterator
	 *            iterator that contains numbers to start from.
	 */
	private void setup(DoubleRangeIterator iterator) {
		super.init(iterator);
		this.min = iterator.getMin();
		this.max = iterator.getMax();
	}

	/**
	 * Constructs a DoubleGenerator over all doubles with interval of 0.1.
	 * 
	 * @return a new DoubleGenerator object.
	 */
	public static DoubleGenerator make() {
		return new DoubleGenerator();
	}

	/**
	 * Constructs a new DoubleGenerator over {minObj, minObj+stepObj, ...,
	 * maxObj-stepObj, maxObj}.
	 * 
	 * @param minObj
	 *            min value over all doubles
	 * @param maxObj
	 *            max value over all doubles
	 * @param stepObj
	 *            interval between two values
	 * @return a new DoubleGenerator object.
	 */
	public static DoubleGenerator makeByBound(Object minObj, Object maxObj, Object stepObj) {
		double min = NumberUtil.doubleFrom(minObj, -Double.MAX_VALUE);
		double max = NumberUtil.doubleFrom(maxObj, Double.MAX_VALUE);
		double step = NumberUtil.doubleFrom(stepObj, 0.1);
		return makeByBound(min, max, step);
	}

	/**
	 * Constructs a new DoubleGenerator over {min, min+step, ..., max-step,
	 * max}.
	 * 
	 * @param min
	 *            min value over all doubles
	 * @param max
	 *            max value over all doubles
	 * @param step
	 *            interval between two values
	 * @return a new DoubleGenerator object.
	 */
	public static DoubleGenerator makeByBound(double min, double max, double step) {
		return new DoubleGenerator(min, max, step);
	}

	/**
	 * Constructs a new DoubleGenerator over [startObj,
	 * startObj+stepObj*countObj].
	 * 
	 * @param startObj
	 *            min/max value over all doubles.
	 * @param stepObj
	 *            interval between two values.
	 * @param countObj
	 *            number of elements.
	 * @return a new DoubleGenerator object.
	 */
	public static DoubleGenerator makeByCount(Object startObj, Object stepObj, Object countObj) {
		double start = NumberUtil.doubleFrom(startObj, -Double.MAX_VALUE);
		BigInteger count;
		try {
			count = new BigInteger("" + countObj);
		} catch (Exception e) {
			count = new BigInteger("" + Long.MAX_VALUE);
		}
		double step = NumberUtil.doubleFrom(stepObj, new BigDecimal(Double.MAX_VALUE).multiply(new BigDecimal(2))
				.divide(new BigDecimal(count), new MathContext(2)).doubleValue());
		return makeByCount(start, step, count);
	}

	/**
	 * Constructs a new DoubleGenerator over [start, start+step*count].
	 * 
	 * @param start
	 *            min/max value over all doubles.
	 * @param step
	 *            interval between two values.
	 * @param count
	 *            number of elements.
	 * @return a new DoubleGenerator object.
	 */
	public static DoubleGenerator makeByCount(double start, double step, BigInteger count) {
		return new DoubleGenerator(start, step, count);
	}

	@Override
	public Object defaultList() {
		return new String[] { "0.0", "1.0", "-1.0", "0..", "0.1.", "0.1x.", Double.toString(Double.MIN_NORMAL),
				Double.toString(Double.POSITIVE_INFINITY), Double.toString(Double.NEGATIVE_INFINITY),
				Double.toString(Double.NaN), Double.toString(max), Double.toString(min), Double.toString(0.0),
				Double.toString(1.0), Double.toString(-1.0) };
	}

	@Override
	public String nextRandom() {
		double val;
		/*
		 * For method nextDouble(origin, bound), if origin is a negative number
		 * with a big absolute value and bound is a positive number with a big
		 * absolute value, nextDouble always generates the same value. So
		 * negative numbers and positive numbers are handled separately.
		 */
		if (min < 0 && max > 0) {
			int sign = lr.nextInt(2);
			if (sign == 1) {
				val = lr.nextDouble(min, 0);
			} else {
				val = lr.nextDouble(0, max);
			}
		} else {
			val = lr.nextDouble(min, max);
		}
		return Double.toString(val);
	}
}
