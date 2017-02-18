package contest.winter2017;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import contest.winter2017.parameter.FormattedStringGenerator;
import contest.winter2017.parameter.Generator;
import contest.winter2017.parameter.StringGenerator;

@SuppressWarnings("rawtypes")
public class EnumerationParameter extends Parameter {
	private List<Generator> gs;

	public EnumerationParameter(Map inputMap) {
		super(inputMap);
		gs = new ArrayList<>();
		for (String e : Parameters.getEnumerationValues(inputMap))
			if (e.matches(FormattedStringGenerator.MATCH_PATTERN))
				gs.add(FormattedStringGenerator.make(e));
			else
				gs.add(StringGenerator.makeWithConstant(e, false));
	}

	@Override
	List<Generator> generators() {
		return gs;
	}

	@Override
	public String next() {
		throw new UnsupportedOperationException("Not supported");
	}
}
