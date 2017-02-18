package contest.winter2017.parameter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import contest.winter2017.util.NumberUtil;

/**
 * Iterator that supports generator. This iterator contains at least no element,
 * at most 3 element, and the the reset will be used to derive new
 * BinaryAccessingIterator(s). Once it iterates through min, max, and mid (if
 * they exists), the other class shall ask for the sub iterators.
 * 
 * @author Apollonian
 *
 * @param <E>
 *            type of elements in this.
 */
public abstract class BinaryAccessingIterator<E> implements Iterator<E> {
	/** Number of elements in the iterator. */
	public enum CountCategory {
		/** No element. */
		ZERO(0),
		/** One element. */
		ONE(1),
		/** Two elements. */
		TWO(2),
		/** Three elements. */
		THREE(3),
		/** More than three elements. */
		MORE(5);

		/** Associated value: number of elements. */
		private int count;

		/**
		 * Constructs a new case in this enumeration.
		 * 
		 * @param count
		 *            number of elements.
		 */
		private CountCategory(int count) {
			this.count = count;
		}

		/**
		 * @return associated value.
		 */
		public int getValue() {
			return count;
		}

		/**
		 * Get enumeration option based on number of elements in a collection.
		 * 
		 * @param obj
		 *            Number of elements, a whole number.
		 * @return CountCategory that describes a collection having obj elements
		 *         in it.
		 */
		public static CountCategory from(Object obj) {
			int val = Math.abs(NumberUtil.intFrom(obj, 5));
			if (val == 0) {
				return ZERO;
			} else if (val == 1) {
				return ONE;
			} else if (val == 2) {
				return TWO;
			} else if (val == 3) {
				return THREE;
			} else {
				return MORE;
			}
		}
	}

	/** @return min element in this. */
	abstract E getMin();

	/** @return max element in this. */
	abstract E getMax();

	/** @return middle element in this. */
	abstract E getMid();

	/** Lazy initializing number of elements, access through _count(). */
	private int _count = -1;

	/** @return an object that represents number of items available in this. */
	public abstract Object count();

	/** @return number of elements. */
	private int _count() {
		if (_count == -1) {
			_count = CountCategory.from(count()).getValue();
		}
		return _count;
	}

	/**
	 * Checks if this has sub iterator(s) or not.
	 * 
	 * @return {@code true} if at least has one sub iterator.
	 */
	public final boolean hasSubIterator() {
		return hasLeftSubIterator() || hasRightSubIterator();
	};

	/** @return smaller sub iterator between (min, mid) */
	abstract BinaryAccessingIterator<E> leftSubIterator();

	/** @return bigger sub iterator between (mid, max) */
	abstract BinaryAccessingIterator<E> rightSubIterator();

	/**
	 * Checks if this has a sub iterator between (min, mid)
	 * 
	 * @return {@code true} if this has a smaller sub iterator, and has at least
	 *         1 element.
	 */
	public final boolean hasLeftSubIterator() {
		BinaryAccessingIterator<E> left = leftSubIterator();
		return left != null && left._count() > 0;
	}

	/**
	 * Checks if this has a sub iterator between (mid, max)
	 * 
	 * @return {@code true} if this has a smaller sub iterator, and has at least
	 *         1 element.
	 */
	public final boolean hasRightSubIterator() {
		BinaryAccessingIterator<E> right = rightSubIterator();
		return right != null && right._count() > 0;
	}

	/**
	 * A queue of sub iterators. For leftSubIterator and rightSubIterator, if it
	 * is not null, it will be in the queue.
	 * 
	 * @return ArrayList of sub iterators.
	 */
	public final Iterable<BinaryAccessingIterator<E>> subIterators() {
		ArrayList<BinaryAccessingIterator<E>> list = new ArrayList<BinaryAccessingIterator<E>>(2);
		if (hasLeftSubIterator()) {
			list.add(leftSubIterator());
		}
		if (hasRightSubIterator()) {
			list.add(rightSubIterator());
		}
		return list;
	}

	/** Internal state. */
	private int state;

	@Override
	public final boolean hasNext() {
		return state < 3 && _count() > state;
	}

	@Override
	public final E next() {
		int _state = state++;
		if (_state == 0) {
			return getMin();
		} else if (_state == 1 && _count() > 1) {
			return getMax();
		} else if (_state == 2 && _count() > 2) {
			return getMid();
		} else {
			throw new NoSuchElementException("Please use subRanges to get more elements, if avaliable");
		}
	}

	@Override
	public String toString() {
		return getMin() + "..." + getMax();
	}
}
