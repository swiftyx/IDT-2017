package contest.winter2017.parameter;

import java.util.List;

import org.junit.Test;

public class StringGeneratorTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testDefaultList() {
		for (Object object : (List<String>) StringGenerator.makeRandom().defaultList()) {
			System.out.println(object);
		}
	}

	@Test
	public void testUnicodes() {
		for (String s : StringGenerator.unicodes())
			System.out.println(s);
	}

}
