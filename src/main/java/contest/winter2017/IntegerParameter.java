package contest.winter2017;

import java.util.Map;

import contest.winter2017.parameter.IntegerGenerator;

public class IntegerParameter extends ContinuousParameter<Integer> {

	@SuppressWarnings("rawtypes")
	public IntegerParameter(Map inputMap) {
		super(inputMap,
          IntegerGenerator.makeByBound((Integer) Parameters.getMin(inputMap),
                                 (Integer) Parameters.getMax(inputMap)));
	}

}
