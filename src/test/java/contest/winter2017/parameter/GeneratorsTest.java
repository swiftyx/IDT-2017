package contest.winter2017.parameter;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import contest.winter2017.Tester;

@SuppressWarnings("rawtypes")
public class GeneratorsTest {

	@Test
	public void testIntegerGenerator() {
		gen100(IntegerGenerator.make());
		genAll(IntegerGenerator.makeByBound("-1", new BigInteger("3")));
	}

	@Test
	public void testDoubleGenerator() {
		gen100(DoubleGenerator.make());
		genAll(DoubleGenerator.makeByBound(-Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE / 2));
		genAll(DoubleGenerator.makeByCount(new BigInteger("0"), new BigDecimal(-2), new BigInteger("4")));
	}

	/** Make sure random numbers are truly random. */
	@Test
	public void testNextRandom() {
		DoubleGenerator g = DoubleGenerator.make();
		for (int i = 0; i < 100; i++)
			System.out.println(g.nextRandom());
	}

	@Test
	public void testCharacterGenerator() {
		gen100(CharacterGenerator
				.makeWithCharSets(new CharacterSet[] { CharacterSet.LOWER_CASE, CharacterSet.UPPER_CASE }));
	}

	@Test
	public void testStringGenerator() {
		gen100(StringGenerator.makeRandom());
	}

	@Test
	public void testStringConstantGenerator() {
		gen100(StringGenerator.makeWithConstant("fibonacci", true));
	}

	@Test
	public void testFormattedGenerator() {
		String I_P = FormattedStringGenerator.REPLACE_INT;
		String S_P = FormattedStringGenerator.REPLACE_STRING;
		{
			FormattedStringGenerator g = FormattedStringGenerator.make(I_P);
			genF(g);
		}
		{
			FormattedStringGenerator g = FormattedStringGenerator.make(S_P);
			genF(g);
		}
		{
			FormattedStringGenerator g = FormattedStringGenerator.make("integer=" + I_P);
			genF(g);
		}
		{
			FormattedStringGenerator g = FormattedStringGenerator.make("name=" + S_P);
			genF(g);
		}
		{
			String format = "no: " + I_P + ", desc: " + S_P;
			FormattedStringGenerator g = FormattedStringGenerator.make(format);
			genF(g);
		}
		{
			String format = "no1: " + I_P + ", desc1: " + S_P + ", desc2: " + S_P + ", no2: " + I_P;
			FormattedStringGenerator g = FormattedStringGenerator.make(format);
			genF(g);
		}
	}

	// MARK: Helpers

	private void genF(Generator g) {
		System.out.println(Tester.HORIZONTAL_LINE);
		gen10(g);
	}

	private void gen10(Generator g) {
		gen(g, 10);
	}

	private void gen100(Generator g) {
		gen(g, 100);
	}

	private void gen(Generator g, int count) {
		for (int i = 0; i < count; i++)
			System.out.println(g.nextFormatted());
	}

	/**
	 * Test by going over all predefined, and 3 random values.
	 * 
	 * @param generator
	 *            Generator to test
	 */
	public void genAll(Generator generator) {
		while (generator.hasNext()) {
			System.out.println(generator.nextFormatted());
		}
		System.out.println("--[[Start Random]]--");
		for (int i = 0; i < 3; i++) {
			System.out.println(generator.nextRandom());
		}
	}
}
