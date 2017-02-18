package contest.winter2017.parameter;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class FormattedStringGeneratorTest {

	@Test
	public void testInvalidFormat() {
		try {
			FormattedStringGenerator.make(null);
			Assert.fail();
		} catch (NullPointerException ex) {
			System.out.println(ex.getMessage());
		}

		try {
			FormattedStringGenerator.make("XX");
		} catch (IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
		}
	}

	@Test
	public void testDollarSign() {
		FormattedStringGenerator g = FormattedStringGenerator
				.make("--FormattedStringWithString<<REPLACE_ME_INT>>=<<REPLACE_ME_STRING>>");

		List<String> dollarSignList = new ArrayList<>();
		dollarSignList.add("0");
		dollarSignList.add("\\\\p{Lower}$");
		String result = g.getFormattedParameter(dollarSignList);
		System.out.println(result);

		System.out.println(g.nextFormatted());
	}
}
