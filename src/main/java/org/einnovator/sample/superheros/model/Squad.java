/**
 * 
 */
package org.einnovator.sample.superheros.model;

/**
 * A {@code Squad}.
 *
 */
public enum Squad {
	MARVEL("Marvel"),
	DC("DC");
	

	private final String displayValue;
	
	Squad(String displayValue) {
		this.displayValue = displayValue;
	}

	
	public String getDisplayValue() {
		return displayValue;
	}

	public String getLabel() {
		return displayValue;
	}

	public static Squad parse(String s) {
		for (Squad e: Squad.class.getEnumConstants()) {
			if (e.toString().equalsIgnoreCase(s)) {
				return e;
			}
		}
		return null;
	}
	
	public static Squad parse(String s, Squad defaultValue) {
		Squad value = parse(s);
		return value!=null ? value: defaultValue;
	}


}
