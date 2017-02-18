package contest.winter2017;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parameters used to execute jars are tricky things (think command line flags),
 * so we developed a ParameterFactory class to help you get the parameter types
 * needed to execute the given jar. Why are executable jar command line
 * parameters/arguments tricky? Because they can be fixed (static) or dependent
 * (dynamic).
 * 
 * Fixed parameters are fairly simple. When a jar simply takes a fixed
 * number/type of parameters as inputs (e.g. java -jar isXDivisibleByY.jar 100
 * 10), the order of those two inputs matters, and the types are fixed.
 * 
 * Dependent parameters occur when subsequent parameter types/values depend upon
 * previous types/values. Often times, there are multiple options at each of the
 * levels. For example, take the following command: java -jar
 * randomGenerator.jar --randomrange start=100 stop=1000 step=0.5 The first
 * argument (--randomrange) was one possibility from several options (--shuffle,
 * --randomint, or --sample). The second, third, and fourth arguments are a
 * result of selecting --randomrange (dependent upon the first parameter), and
 * they could be in any order.
 * 
 * ParameterFactory is our attempt to reduce the complexity related to dependent
 * parameters. Parameter definitions are built in an iterative manner: on each
 * iteration that it will return all of the potential options for that parameter
 * index (each time you call, you pass in the sum of previous selections to
 * build up the parameter definition dynamically). The method that we wrote to
 * help is called getNext(List<String> previousParameterValues);
 * 
 * YOU ARE WELCOME TO CHANGE THIS CLASS, INCLUDING THE APPROACH. KEEP IN MIND
 * THAT YOU CAN'T CHANGE THE EXISTING FORMAT IN THE BLACK-BOX JARS THOUGH.
 * 
 * @author IDT
 */
public class ParameterFactory {

	private static final String FIXED_KEY = "fixed parameter list";
	private static final String DEPENDENT_KEY = "dependent parameters";

	private Logger log = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("rawtypes")
	private Map inputMap;
	private Map<String, Object> dependentParametersMap;

	@SuppressWarnings("rawtypes")
	private List<Map> fixedParamList;
	private boolean bounded;

	private List<Parameter> fixed;
	private Map<String, Object> dependent;

	/**
	 * ctr for Parameter Factory class
	 * 
	 * @param inputMap
	 *            - input map that describes all of the parameter data
	 *            associated with an executable jar
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ParameterFactory(Map inputMap) {
		this.inputMap = inputMap;
		this.fixedParamList = (List) this.inputMap.get(FIXED_KEY);
		this.dependentParametersMap = (Map) this.inputMap.get(DEPENDENT_KEY);
		if (this.fixedParamList != null) {
			this.bounded = true;
		} else {
			this.bounded = false;
		}

		if (dependentParametersMap != null) {
			dependent = new HashMap<>();
			for (Map.Entry<String, Object> mapEntry : this.dependentParametersMap.entrySet()) {
				String key = mapEntry.getKey();
				Object obj = mapEntry.getValue();
				if (obj instanceof Map) {
					Parameter p = Parameters.makeWith((Map) obj);
					if (p != null)
						dependent.put(key, p);
				} else {
					List<Parameter> list = new ArrayList<>();
					for (Map paramMap : (List<Map>) obj) {
						Parameter p = Parameters.makeWith(paramMap);
						if (p != null)
							list.add(p);
					}
					dependent.put(key, list);
				}
			}
		}

		if (fixedParamList != null) {
			fixed = new ArrayList<>();
			for (int i = 0; i < fixedParamList.size(); i++) {
				Parameter p = Parameters.makeWith(fixedParamList.get(i));
				if (p != null)
					fixed.add(p);
			}
		}

		log.info("{}", toString());
	}

	/**
	 * Method to test if the parameters associated with this jar are fixed (aka
	 * bounded)
	 * 
	 * @return true if the parameters are fixed (bounded) and false if they are
	 *         not
	 */
	public boolean isBounded() {
		return this.bounded;
	}

	/**
	 * Method to deal with the complexity of dependent parameters. Also handles
	 * fixed parameters. For more information about dependent and fixed
	 * parameters, see explanation at the top of this class. We are essentially
	 * determining the potential parameters for a given index, and that index is
	 * determined by the values in previous ParameterValues (hence, we call this
	 * iteratively and build the definition). This code is certainly fair game
	 * for change.
	 * 
	 * @param previousParameterValues
	 *            - since this method is used iteratively to build up the
	 *            parameter definitions, this is the accumulated parameters that
	 *            have been passed in until now
	 * @return List of Parameter objects containing all metadata known about the
	 *         each Parameter
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Parameter> getNext(List<String> previousParameterValues) {
		// ultimately we are returning all possible parameters for a given index
		// (since we could be dealing with dependent parameters and enumeration
		// parameters)
		List<Parameter> possibleParamsList = new ArrayList<Parameter>();

		StringBuffer sb = new StringBuffer();
		for (String paramString : previousParameterValues) {
			sb.append(" " + paramString);
		}
		String currentParamsString = sb.toString();

		// process all dependent parameters
		if (this.dependent != null) {
			for (Map.Entry<String, Object> mapEntry : this.dependent.entrySet()) {
				String key = mapEntry.getKey();
				if (currentParamsString.matches(key) || (key.isEmpty() && currentParamsString.isEmpty())) {
					Object obj = mapEntry.getValue();
					if (obj instanceof Parameter) {
						possibleParamsList.add((Parameter) obj);
					} else {
						possibleParamsList.addAll((List) obj);
					}
				}
			}
			// if there are no dependent parameters, process the fixed
			// parameters
		} else {
			if (previousParameterValues.size() < fixed.size()) {
				Parameter fp = fixed.get(previousParameterValues.size());
				possibleParamsList.add(fp);
			}
		}

		log.debug("prev: {}", currentParamsString);
		log.debug("list: {}", possibleParamsList);
		// return the list of possible parameters for this index
		return possibleParamsList;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName() + ":\n");
		sb.append("\t" + Tester.TESTS_KEY + ": " + inputMap.get(Tester.TESTS_KEY) + "\n");

		if (fixedParamList != null)
			sb.append("\t" + FIXED_KEY + ": " + fixedParamList + "\n");

		if (dependentParametersMap != null) {
			sb.append("\t" + DEPENDENT_KEY + ":\n");
			for (Map.Entry<String, Object> mapEntry : dependentParametersMap.entrySet()) {
				String key = mapEntry.getKey();
				Map val = (Map) mapEntry.getValue();
				if (Parameters.isEnumeration(val)) {
					sb.append("\t\t" + key + "[" + Parameters.getEnumerationValues(val).size() + "]: " + val + "\n");
				} else {
					sb.append("\t\t" + key + ": " + val + "\n");
				}
			}
		}
		String text = sb.toString();
		int n = text.length();
		if (text.charAt(n - 1) == '\n')
			text = text.substring(0, n - 1);

		return text;
	}
}
