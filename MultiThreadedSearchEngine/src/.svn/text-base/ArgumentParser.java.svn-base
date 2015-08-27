import java.util.HashMap;

/**
 * Class that handles parsing an array of arguments into flag/value pairs. A
 * flag is considered to be a non-null String that starts with a "-" dash
 * symbol. A value optionally follows a flag, and must not start with a "-" dash
 * symbol.
 * 
 * @author: Diana Ly
 */
public class ArgumentParser {

	/** Stores flag/value pairs of arguments. */
	private final HashMap<String, String> argumentMap;

	public ArgumentParser(String[] args) {
		argumentMap = new HashMap<String, String>();
		parseArgs(args);
	}

	/**
	 * Parses the provided array of arguments into flag/value pairs, storing the
	 * results in an internal map.
	 * 
	 * @param arguments
	 *            to parse
	 */
	private void parseArgs(String[] arguments) {
		if (arguments == null) {
			System.out.println("Error: No arguments given.");
		} else {
			String flag = null;
			for (String text : arguments) {
				if (isFlag(text)) {
					flag = text;
					if (hasFlag(flag)) {
						continue;
					} else {
						argumentMap.put(flag, null);
					}
				} else if (isValue(text)) {
					argumentMap.put(flag, text);
					flag = null;
				}
			}
		}
	}

	/**
	 * Tests whether the provided String is a flag, i.e. whether the String is
	 * non-null, starts with a "-" dash, and has at least one character
	 * following the dash.
	 * 
	 * @param text
	 *            to test
	 * @return <code>true</code> if the text is non-null, start with the "-"
	 *         dash symbol, and has a flag name of at least one character
	 */
	public static boolean isFlag(String text) {
		return (text != null && text.startsWith("-") && !text.trim().isEmpty() && text
				.length() >= 2);
	}

	/**
	 * Tests whether the provided String is a value, i.e. whether the String is
	 * non-null, non-empty, and does NOT start with a "-" dash.
	 * 
	 * @param text
	 *            to test
	 * @return <code>true</code> if the text is non-null, non-empty, and does
	 *         NOT start with the "-" dash symbol
	 */
	public static boolean isValue(String text) {
		return (text != null && !text.trim().isEmpty() && !text.startsWith("-"));
	}

	/**
	 * Returns the number of flags stored in the argument map.
	 * 
	 * @return number of flags stored in the argument map
	 */
	public int numFlags() {
		return argumentMap.size();
	}

	/**
	 * Checks whether the provided flag exists in the argument map.
	 * 
	 * @param flag
	 *            to check for
	 * @return <code>true</code> if the flag exists
	 */
	public boolean hasFlag(String flag) {
		return (argumentMap.containsKey(flag));
	}

	/**
	 * Checks whether the provided flag has an associated non-null value.
	 * Returns <code>false</code> if there is no value for the flag, or if the
	 * flag does not exist.
	 * 
	 * @param flag
	 *            to check for value
	 * @return <code>true</code> if the flag has a non-null value, or
	 *         <code>false</code> if the value or flag does not exist
	 */
	public boolean hasValue(String flag) {
		return (flag != null && argumentMap.get(flag) != null);
	}

	/**
	 * Returns the value associated with a flag, or <code>null</code> if the
	 * flag does not exist, or the flag does not have an associated value.
	 * 
	 * @param flag
	 *            to fetch associated value
	 * @return value of the flag if it exists, or <code>null</code>
	 */
	public String getValue(String flag) {
		return argumentMap.get(flag);
	}

	/**
	 * Checks if the value in the given flag has a valid integer
	 * 
	 * @param flag
	 * @return <code>true</code> if it is a valid integer, or <code>false</code>
	 *         if it is not a valid integer
	 */
	public boolean hasValidInteger(String flag) {
		boolean isInt = false;
		if (flag == null || flag.isEmpty()) {
			isInt = false;
		} else {
			try {
				int num = Integer.parseInt(getValue(flag));
				if (num > 0) {
					isInt = true;
				}
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return isInt;
	}

	/**
	 * Gets the integer value of the given flag
	 * 
	 * @param flag
	 * @return integer value of the given flag
	 */
	public int getInteger(String flag) {
		return Integer.parseInt(getValue(flag));
	}

	/**
	 * Checks whether the flag is null
	 * 
	 * @param flag
	 * @return <code>true</code> if the flag is not null and <code>false</code>
	 *         if it is
	 */
	public boolean hasURL(String flag) {
		return getValue(flag) != null ? true : false;
	}

	/**
	 * Checks whether port exists
	 * 
	 * @param flag
	 * @return <code>true</code> if the port is not null and <code>false</code>
	 *         if it is
	 */
	public boolean hasPort(String flag) {
		return getValue(flag) != null ? true : false;
	}
}