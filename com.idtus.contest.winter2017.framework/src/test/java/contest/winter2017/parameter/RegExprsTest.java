package contest.winter2017.parameter;

import org.junit.Test;

public class RegExprsTest {

	@Test
	public void testCreate() {
		for (String s : RegExprs.regExprs())
			System.out.println(s);
	}

}
