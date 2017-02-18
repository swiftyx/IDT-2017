package contest.winter2017;

import java.util.Map;

import contest.winter2017.parameter.StringGenerator;

@SuppressWarnings("rawtypes")
public class StringParameter extends ContinuousParameter<String> {

	public StringParameter(Map inputMap) {
		super(inputMap, StringGenerator.makeRandom());
	}

	public StringParameter(Map inputMap, String constant, boolean hasArg) {
		super(inputMap, StringGenerator.makeWithConstant(constant, hasArg));
	}

}
