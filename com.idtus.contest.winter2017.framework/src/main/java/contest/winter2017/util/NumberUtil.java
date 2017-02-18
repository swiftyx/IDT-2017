package contest.winter2017.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/** See {@link NumberUtilTest} to figure out how this is working */
public class NumberUtil {
	/**
	 * Extracts a possible integer value from an object.
	 * 
	 * @param obj
	 *            Object that might be an integer
	 * @param defaultValue
	 *            return value if the process failed.
	 * @return int value of object if it is expressible by int; defaultValue
	 *         otherwise.
	 */
	public static int intFrom(Object obj, int defaultValue) {
		if (obj instanceof Integer) {
			return (Integer) obj;
		}
		BigInteger instance;
		try {
			if (obj instanceof BigInteger) {
				instance = (BigInteger) obj;
			} else {
				instance = new BigInteger("" + obj);
			}
			return instance.intValueExact();
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Extracts a possible double value from an object.
	 * 
	 * @param obj
	 *            Object that might be a double
	 * @param defaultValue
	 *            return value if the process failed.
	 * @return double value of object if it is expressible by double;
	 *         defaultValue otherwise.
	 */
	public static double doubleFrom(Object obj, double defaultValue) {
		double val = defaultValue;
		if (obj instanceof Double) {
			val = (Double) obj;
		}
		BigDecimal instance;
		try {
			if (obj instanceof BigDecimal) {
				instance = (BigDecimal) obj;
			} else {
				instance = new BigDecimal("" + obj);
			}
			val = instance.doubleValue();
		} catch (Exception e) {
			/* Use default value */
		}
		return val;
	}
}
