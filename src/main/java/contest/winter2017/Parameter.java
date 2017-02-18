package contest.winter2017;

import java.util.List;
import java.util.Map;

import com.google.common.base.MoreObjects;

import contest.winter2017.parameter.Generator;

/**
 * Class that represents a single parameter for an executable jar.
 * 
 * @author IDT
 */
public abstract class Parameter {
	/**
	 * Input Map associated with this parameter
	 */
	@SuppressWarnings("rawtypes")
	protected final Map inputMap;

	/**
	 * Ctr for Parameter
	 * 
	 * @param inputMap
	 *            - map containing parameter meta data
	 */
	@SuppressWarnings("rawtypes")
	public Parameter(Map inputMap) {
		this.inputMap = inputMap;
	}

	/**
	 * Getter for the optionality of the parameter
	 * 
	 * @return boolean true indicates the parameter is optional
	 */
	public boolean isOptional() {
		if (inputMap.get("optional") != null) {
			return (Boolean) inputMap.get("optional");
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("inputMap", inputMap).toString();
	}

	abstract List<Generator> generators();

	public abstract String next();
}
