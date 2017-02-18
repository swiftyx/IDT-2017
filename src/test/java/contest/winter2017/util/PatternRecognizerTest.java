package contest.winter2017.util;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import contest.winter2017.Output;

public class PatternRecognizerTest {

	@Test
	public void test() {
		ensure("regex");
		ensure("Regexp");
		ensure("REGULAR exp");
		ensure("regular Expression");
	}

	public void ensure(String s) {
		assertTrue(PatternRecognizer.literallyContainsRegex(s));
	}

	@Test
	public void find() {
		format("] 12, 3");
		format("[1,    2, 3,4]");
		format("a|b|c");
		format("\"test\" matches regex:te(?i)st");
		format("\"㽱聆䟗铓ὖ犘ꠎ觱ᨯ섧⩶\" does NOT match regex:냍𨢢싉攦");
		format("----ENVIRONMENT-----------------Runtime: Oracle Corporation Java HotSpot(TM) 64-Bit Server VM 25.112-b16 ----ARGUMENTS-------------------input: <<REPLACE_ME_STRING>>password: <<REPLACE_ME_STRING>>stringOutputType: <<REPLACE_ME_STRING>>----OUTPUT----------------------f3Dl1/iQWAr+QY44f/RhxnaOhq7RAlYt/zgHQtcYuhE=");
		format("DIGEST ALGORITHMS:   [MD2, MD5, SHA, SHA-224, SHA-256, SHA-384, SHA-512]PBE ALGORITHMS:      [PBEWITHHMACSHA1ANDAES_128, PBEWITHHMACSHA1ANDAES_256, PBEWITHHMACSHA224ANDAES_128, PBEWITHHMACSHA224ANDAES_256, PBEWITHHMACSHA256ANDAES_128, PBEWITHHMACSHA256ANDAES_256, PBEWITHHMACSHA384ANDAES_128, PBEWITHHMACSHA384ANDAES_256, PBEWITHHMACSHA512ANDAES_128, PBEWITHHMACSHA512ANDAES_256, PBEWITHMD5ANDDES, PBEWITHMD5ANDTRIPLEDES, PBEWITHSHA1ANDDESEDE, PBEWITHSHA1ANDRC2_128, PBEWITHSHA1ANDRC2_40, PBEWITHSHA1ANDRC4_128, PBEWITHSHA1ANDRC4_40] stderr of execution: ");
	}

	public void format(String s) {
		List<String> list = PatternRecognizer.getEnumeration(s);
		if (list != null) {
			for (String string : list)
				System.out.println(string);
		}
		System.out.println("-----");
	}

	@Test
	public void testAnalyze() {
		PatternRecognizer recognizer = new PatternRecognizer();
		Output output = new Output(
				"DIGEST ALGORITHMS:   [MD2, MD5, SHA, SHA-224, SHA-256, SHA-384, SHA-512]PBE ALGORITHMS:      [PBEWITHHMACSHA1ANDAES_128, PBEWITHHMACSHA1ANDAES_256, PBEWITHHMACSHA224ANDAES_128, PBEWITHHMACSHA224ANDAES_256, PBEWITHHMACSHA256ANDAES_128, PBEWITHHMACSHA256ANDAES_256, PBEWITHHMACSHA384ANDAES_128, PBEWITHHMACSHA384ANDAES_256, PBEWITHHMACSHA512ANDAES_128, PBEWITHHMACSHA512ANDAES_256, PBEWITHMD5ANDDES, PBEWITHMD5ANDTRIPLEDES, PBEWITHSHA1ANDDESEDE, PBEWITHSHA1ANDRC2_128, PBEWITHSHA1ANDRC2_40, PBEWITHSHA1ANDRC4_128, PBEWITHSHA1ANDRC4_40] stderr of execution: ",
				"");
		recognizer.analyze(output);
		System.out.println(recognizer.map);
		System.out.println("-----");

	}

	@Test
	public void testReassign() {
		System.out.println(PatternRecognizer.reassign("hi", "1=2", "3"));
		System.out.println(PatternRecognizer.reassign("hi", "1:2", "3"));
		System.out.println(PatternRecognizer.reassign("hi", "1", "3"));
		System.out.println("-----");
	}
}
