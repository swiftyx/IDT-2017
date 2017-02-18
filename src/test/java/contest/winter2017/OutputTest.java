package contest.winter2017;

import org.junit.Test;

import com.google.common.truth.Truth;

public class OutputTest {

	@Test
	public void testIsError() {
		{

			Output output = new Output("standard output text", "standard error text");
			Truth.assertThat(output.getErrors().size()).isEqualTo(0);
		}

		{
			Output output = new Output("standard output text", "xxExceptionxx");
			Truth.assertThat(output.getErrors().size()).isEqualTo(1);
		}

		{
			Output output = new Output("RuntimeException", "");
			Truth.assertThat(output.getErrors().size()).isEqualTo(1);
		}

		{
			String errFromRegexPatternMatch = "Exception in thread \"main\" java.lang.ArrayIndexOutOfBoundsException: 1  at regexpatternmatch.Pattern.main(Pattern.java:753)";
			String errFromCommandLineEncryption = "----ERROR-----------------------java.security.NoSuchAlgorithmException: one SecretKeyFactory not available";
			Output output = new Output(errFromRegexPatternMatch, errFromCommandLineEncryption);
			Truth.assertThat(output.getErrors().size()).isEqualTo(2);
		}
	}
}
