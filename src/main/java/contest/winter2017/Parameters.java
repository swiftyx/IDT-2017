package contest.winter2017;

import java.util.List;
import java.util.Map;

import com.google.common.base.Verify;

@SuppressWarnings("rawtypes")
public class Parameters {

	public static Parameter makeWith(Map inputMap) {
		if (isEnumeration(inputMap)) {
			return new EnumerationParameter(inputMap);
		} else {
			Class<?> type = getType(inputMap);
			if (type == Integer.class) {
				return new IntegerParameter(inputMap);
			} else if (type == Double.class) {
				return new DoubleParameter(inputMap);
			} else if (type == String.class) {
				String format = getFormat(inputMap);
				return format != null ? new FormattedStringParameter(inputMap, format) : new StringParameter(inputMap);
			} else {
				throw new IllegalArgumentException("unsupported type: {}" + type.getSimpleName());
			}
		}
	}

	/**
	 * Method to find out if this parameter is an enumeration or not, 
	 * meaning that there are multiple options associated with this parameter
	 * @return boolean true if this parameter is an enumeration, false if it is not
	 */
	public static boolean isEnumeration(Map inputMap) {
		return inputMap.get("enumerated values") != null;
	}

	/**
	 * Getter for type of parameter (integer, long, double, float, String, etc)
	 * @return
	 */
	public static Class getType(Map inputMap) {
		Class type = (Class) inputMap.get("type");
		Verify.verify(type != null);
		return type;
	}

	/**
	 * Getter for the format string the parameter has if it has a specific one
	 * @return String with the parameters format <<REPLACE_ME_...>> are included
	 */
	public static String getFormat(Map inputMap) {
		return (String) inputMap.get("format");
	}

	/**
	 * Getter for enumeration values (if this parameter is an enumeration)
	 * 
	 * @return List<String> containing multiple options associated with this parameter
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getEnumerationValues(Map inputMap) {
		return (List<String>) inputMap.get("enumerated values");
	}
	
	/**
	* Getter for the min value associated with this parameter (if one exists)
	* 
	* @return Object representing the minimum value associated with this parameter
	*/
	public static Object getMin(Map inputMap) {
		return inputMap.get("min");
	}

	/**
	* Getter for the max value associated with this parameter (if one exists)
	* 
	* @return Object representing the maximum value associated with this parameter
	*/
	public static Object getMax(Map inputMap) {
		return inputMap.get("max");
	}
}
