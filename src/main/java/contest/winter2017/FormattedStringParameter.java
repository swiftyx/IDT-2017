package contest.winter2017;

import java.util.Map;

import contest.winter2017.parameter.FormattedStringGenerator;

public class FormattedStringParameter extends ContinuousParameter<String> {

	@SuppressWarnings("rawtypes")
	public FormattedStringParameter(Map inputMap, String format) {
		super(inputMap, FormattedStringGenerator.make(format));
	}

}
