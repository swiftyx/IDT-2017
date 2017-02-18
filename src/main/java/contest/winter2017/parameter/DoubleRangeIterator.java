package contest.winter2017.parameter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * Binary accessing iterator over doubles
 */
class DoubleRangeIterator extends BinaryAccessingIterator<Double> {
	/** Min value. */
	private final BigDecimal min;
	/** Max value. */
	private final BigDecimal max;
	/** The interval between the adjacent value. */
	private final BigDecimal step;
	/** Number of elements. */
	private final BigInteger count;
	/** Precision. */
	private final MathContext context;

	/**
	 * Constructs a DoubleRangeIterator over all doubles with interval of 0.1.
	 */
	private DoubleRangeIterator() {
		this(-Double.MAX_VALUE, Double.MAX_VALUE, 0.1);
	}

	/**
	 * Constructs a new DoubleRangeIterator over {min, min+step, ..., max-step,
	 * max}.
	 * 
	 * @param min
	 *            min value over all doubles
	 * @param max
	 *            max value over all doubles
	 * @param step
	 *            interval between two values
	 */
	private DoubleRangeIterator(double min, double max, double step) {
		this.min = new BigDecimal(Math.min(min, max));
		this.max = new BigDecimal(Math.max(min, max));
		this.step = new BigDecimal(Math.abs(step));
		this.context = new MathContext(this.step.scale());
		this.count = this.max.subtract(this.min, context).divide(this.step, context).toBigInteger()
				.add(new BigInteger("1"));
	}

	/**
	 * Constructs a new DoubleRangeIterator over [start, start+step*count].
	 * 
	 * @param start
	 *            min/max value over all doubles.
	 * @param step
	 *            interval between two values.
	 * @param count
	 *            number of elements.
	 */
	private DoubleRangeIterator(double start, double step, BigInteger count) {
		if (step == 0) {
			throw new IllegalArgumentException("Can not generate a finite range with step of 0");
		}
		this.count = count;
		this.step = new BigDecimal(Math.abs(step));
		this.context = new MathContext(this.step.scale());
		if (step > 0) {
			this.min = new BigDecimal(start);
			this.max = this.step.multiply(new BigDecimal(this.count)).add(this.min);
		} else {
			this.max = new BigDecimal(start);
			this.min = this.max.subtract((this.step.multiply(new BigDecimal(this.count))));
		}
	}

	/**
	 * Constructs a DoubleRangeIterator over all doubles with interval of 0.1.
	 * 
	 * @return a new DoubleRangeIterator object.
	 */
	public static DoubleRangeIterator make() {
		return new DoubleRangeIterator();
	}

	/**
	 * Constructs a new DoubleRangeIterator over {min, min+step, ..., max-step,
	 * max}.
	 * 
	 * @param min
	 *            min value over all doubles
	 * @param max
	 *            max value over all doubles
	 * @param step
	 *            interval between two values
	 * @return a new DoubleRangeIterator object.
	 */
	public static DoubleRangeIterator makeByBound(double min, double max, double step) {
		if (min > max) {
			return null;
		}
		return new DoubleRangeIterator(min, max, step);
	}

	/**
	 * Constructs a new DoubleRangeIterator over [start, start+step*count].
	 * 
	 * @param start
	 *            min/max value over all doubles.
	 * @param step
	 *            interval between two values.
	 * @param count
	 *            number of elements.
	 * @return a new DoubleRangeIterator object.
	 */
	public static DoubleRangeIterator makeByCount(double start, double step, BigInteger count) {
		return new DoubleRangeIterator(start, step, count);
	}

	@Override
	public Double getMin() {
		return min.doubleValue();
	}

	@Override
	public Double getMid() {
		return max.subtract(min).divide(new BigDecimal(2), context).add(min).doubleValue();
	}

	@Override
	public Double getMax() {
		return max.doubleValue();
	}

	@Override
	public Object count() {
		return count;
	}

	@Override
	BinaryAccessingIterator<Double> leftSubIterator() {
		double step = this.step.doubleValue();
		return makeByBound(getMin() + step, getMid() - step, step);
	}

	@Override
	BinaryAccessingIterator<Double> rightSubIterator() {
		double step = this.step.doubleValue();
		return makeByBound(getMid() + step, getMax() - step, step);
	}

	@Override
	public String toString() {
		return super.toString() + " by " + step;
	}
}
