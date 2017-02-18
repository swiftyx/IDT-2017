package contest.winter2017.parameter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.collections.iterators.ArrayIterator;

/**
 * Abstract generator that consists of binary accessing iterators. This is a
 * experimental algorithm to generate "random" numbers that guarantees to be
 * unique. To put it simply, we check the minimum, the maximum, the middle, then
 * we repeat this process for sub interval (min,mid) and (mid, max), until no
 * subinterval exists. Theoretically it is checks all the (2*n+1)/(2^m), for
 * each n in {0, m-1, 1, m+2, ... m/2} first. Therefore, through this way the
 * elements generated will scatter across the entire interval.
 * 
 * @param <E>
 *            type of elements this is generating.
 * 
 * @author Apollonian
 */
public abstract class Generator<E> implements Iterator<E>, InfiniteGenerator {
	/**
	 * Iterator to use first to generate the elements, automatically extracted
	 * from defaultList
	 */
	private Iterator<?> predefinedIterator;

	/** A flag to bring randomness. */
	private boolean pickFirst = true;
	/** Current iterator responsible for generating the next element. */
	private BinaryAccessingIterator<E> currentIterator;
	/**
	 * A queue of iterators to be used next, once the current iterator goes out.
	 */
	private List<BinaryAccessingIterator<E>> currentIterators = new LinkedList<BinaryAccessingIterator<E>>();
	/**
	 * A queue of iterators scheduled to use, after all the iterators in
	 * currentIterators goes out.
	 */
	private List<BinaryAccessingIterator<E>> nextIterators = new LinkedList<BinaryAccessingIterator<E>>();

	/**
	 * Setup predefined list.
	 */
	public final void init() {
		setupPredefined();
	}

	/**
	 * Setup predefined list, and set the iterator to use after defaultList to
	 * i.
	 * 
	 * @param i
	 *            iterator containing values to use after defaultList, and
	 *            before normal values.
	 */
	public final void init(BinaryAccessingIterator<E> i) {
		this.currentIterator = i;
		init();
	}

	/**
	 * Defines set of values that always adds to the head of the iteration,
	 * despite other parameters.
	 * 
	 * @return Any object that is/contains values to add to the iteration. null
	 *         values means no desired elements to add to iteration; Iterable,
	 *         Iterator, primitive array objects are converted to iterator;
	 *         other value keeps as its String representation.
	 */
	public abstract Object defaultList();

	/**
	 * Get iterator from abstract method defaultList. All the items in the
	 * iterator will be used first, independent from other conditions.
	 */
	private void setupPredefined() {
		Object preferred = defaultList();
		if (preferred == null) {
			predefinedIterator = null;
		} else if (preferred instanceof Iterable) {
			predefinedIterator = ((Iterable<?>) preferred).iterator();
		} else if (preferred instanceof Iterator) {
			predefinedIterator = (Iterator<?>) preferred;
		} else {
			try {
				predefinedIterator = new ArrayIterator(preferred);
			} catch (Exception e) {
				predefinedIterator = Arrays.asList(preferred).iterator();
			}
		}
	}

	/**
	 * Add a new iterator to the current queue
	 * 
	 * @param e
	 *            iterator to be added to the current iterators queue
	 */
	public final void add(BinaryAccessingIterator<E> e) {
		currentIterators.add(e);
	}

	/**
	 * Add several iterators to the current queue
	 * 
	 * @param c
	 *            a collection containing iterators to be added to the current
	 *            iterators queue
	 */
	public final void addAll(Collection<BinaryAccessingIterator<E>> c) {
		currentIterators.addAll(c);
	}

	@Override
	public final boolean hasNext() {
		return currentIterator != null && (currentIterator.hasNext() || currentIterator.hasSubIterator())
				|| currentIterators != null && currentIterators.size() > 0
				|| nextIterators != null && nextIterators.size() > 0;
	}

	@Override
	public final E next() {
		while (currentIterator == null || !currentIterator.hasNext()) {
			if (currentIterator != null && currentIterator.hasSubIterator()) {
				for (BinaryAccessingIterator<E> iter : currentIterator.subIterators()) {
					if (iter != null) {
						// Accumulate iterators to be used next
						nextIterators.add(iter);
					}
				}
			}
			if (currentIterators.size() <= 0) {
				if (nextIterators.size() <= 0) {
					// No more elements in the iterator
					throw new NoSuchElementException();
				}
				// Start to use iterators in the next queue, and clear the next
				// queue for other iterators
				currentIterators = nextIterators;
				nextIterators = new LinkedList<BinaryAccessingIterator<E>>();
			}
			// Pick either front or rear so we can have some randomness here
			int index = pickFirst ? 0 : currentIterators.size() - 1;
			pickFirst = !pickFirst;
			// Make the iterator current
			currentIterator = currentIterators.remove(index);
		}
		return currentIterator.next();
	}

	/**
	 * Check if the predefined values still has unused value(s).
	 * 
	 * @return {@code true} if the predefined values has more elements
	 */
	public final boolean hasNextPredefined() {
		return predefinedIterator != null && predefinedIterator.hasNext();
	}

	/**
	 * Suggested way to get next element. Returns a predefined value if it has
	 * not been used, or the next value if the iterator still has next element,
	 * or a random value.
	 * 
	 * @return next element formatted as String
	 */
	public final String nextFormatted() {
		if (hasNextPredefined()) {
			return "" + predefinedIterator.next();
		} else if (hasNext()) {
			return "" + next();
		} else {
			return nextRandom();
		}
	}
}
