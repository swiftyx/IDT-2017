package contest.winter2017;

import java.util.Map;

import contest.winter2017.parameter.DoubleGenerator;

public class DoubleParameter extends ContinuousParameter<Double> {
	@SuppressWarnings("rawtypes")
	public DoubleParameter(Map inputMap) {
		super(inputMap, DoubleGenerator.makeByBound(Parameters.getMin(inputMap), Parameters.getMax(inputMap), 0.1));
	}

}
