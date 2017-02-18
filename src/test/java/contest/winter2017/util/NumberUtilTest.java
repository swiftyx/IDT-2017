package contest.winter2017.util;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class NumberUtilTest {

	public static int ERR = -1;

	@Test
	public void testIntConvertion() {
		assertEqualsConvertingInt(1, 1);
		assertEqualsConvertingInt(3, "3");
		assertErrConvertingInt(3.4);

		assertErrConvertingInt(Long.MAX_VALUE);
		assertErrConvertingInt(Long.MIN_VALUE);

		assertEqualsConvertingInt(2, new BigInteger("2"));
		assertErrConvertingInt(new BigInteger("" + Long.MAX_VALUE));

		assertErrConvertingInt(null);
		assertErrConvertingInt("hello");
	}

	private void assertEqualsConvertingInt(int expected, Object actual) {
		assertEquals(expected, NumberUtil.intFrom(actual, ERR));
	}

	private void assertErrConvertingInt(Object actual) {
		assertEqualsConvertingInt(ERR, actual);
	}

	@Test
	public void testDoubleConvertion() {
		assertEqualsConvertingDouble(1.0, 1);
		assertEqualsConvertingDouble(-3.2, "-3.2");
		assertEqualsConvertingDouble(3.4, 3.4);
		assertEqualsConvertingDouble(Long.MAX_VALUE, Long.MAX_VALUE);

		assertEqualsConvertingDouble(2, new BigInteger("2"));
		assertEqualsConvertingDouble(Long.MIN_VALUE, new BigInteger("" + Long.MIN_VALUE));

		assertEqualsConvertingDouble(2.3, new BigDecimal("2.3"));
		assertEqualsConvertingDouble(Double.MAX_VALUE, new BigDecimal(Double.MAX_VALUE).add(new BigDecimal(2)));

		assertErrConvertingDouble(null);
		assertErrConvertingDouble("hello");
	}

	private void assertEqualsConvertingDouble(double expected, Object actual) {
		assertEquals(expected, NumberUtil.doubleFrom(actual, ERR), Double.MIN_NORMAL);
	}

	private void assertErrConvertingDouble(Object actual) {
		assertEqualsConvertingDouble(ERR, actual);
	}

}
