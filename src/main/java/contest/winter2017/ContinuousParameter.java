package contest.winter2017;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import contest.winter2017.parameter.Generator;

public class ContinuousParameter<E> extends Parameter {
	Generator<E> generator;
	List<Generator> generators = new ArrayList<>();

	@SuppressWarnings("rawtypes")
	public ContinuousParameter(Map inputMap, Generator<E> generator) {
		super(inputMap);
		this.generator = generator;
		generators.add(generator);
	}

	@SuppressWarnings("rawtypes")
	@Override
	List<Generator> generators() {
		return generators;
	}
	
	@Override
	public String next() {
		return generator.nextFormatted();
	}
}
