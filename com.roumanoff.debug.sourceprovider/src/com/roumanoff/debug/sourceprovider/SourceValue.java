package com.roumanoff.debug.sourceprovider;

import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.ui.ISources;

/**
 * provide a data model for source provider value<br>
 * also perform the conversion to String to display in a table.
 * 
 * @author Patrick Roumanoff
 * 
 */
class SourceValue {
	static final int[] PRIORITIES = { ISources.ACTIVE_ACTION_SETS,
			ISources.ACTIVE_CONTEXT, ISources.ACTIVE_CURRENT_SELECTION,
			ISources.ACTIVE_EDITOR, ISources.ACTIVE_EDITOR_ID,
			ISources.ACTIVE_MENU, ISources.ACTIVE_PART,
			ISources.ACTIVE_PART_ID, ISources.ACTIVE_SHELL,
			ISources.ACTIVE_SITE, ISources.ACTIVE_WORKBENCH_WINDOW,
			ISources.ACTIVE_WORKBENCH_WINDOW_SHELL,
			ISources.ACTIVE_WORKBENCH_WINDOW_SUBORDINATE, ISources.WORKBENCH,
			ISources.LEGACY_LEGACY, ISources.LEGACY_LOW, ISources.LEGACY_MEDIUM };
	static final String[] PRIORITY_NAMES = { ISources.ACTIVE_ACTION_SETS_NAME,
			ISources.ACTIVE_CONTEXT_NAME,
			ISources.ACTIVE_CURRENT_SELECTION_NAME,
			ISources.ACTIVE_EDITOR_NAME, ISources.ACTIVE_EDITOR_ID_NAME,
			ISources.ACTIVE_MENU_NAME, ISources.ACTIVE_PART_NAME,
			ISources.ACTIVE_PART_ID_NAME, ISources.ACTIVE_SHELL_NAME,
			ISources.ACTIVE_SITE_NAME, ISources.ACTIVE_WORKBENCH_WINDOW_NAME,
			ISources.ACTIVE_WORKBENCH_WINDOW_SHELL_NAME,
			"Workbench window subordinate", "Workbench", "LEGACY", "LOW",
			"MEDIUM" };

	public int priority;
	public String name;
	public Object value;

	public SourceValue(int priority, String name, Object value) {
		this.priority = priority;
		this.name = name;
		this.value = value;
	}

	@Override
	public String toString() {
		return "[" + getName() + ":" + getValue() + ":" + getPriority() + "]";
	}

	public String getName() {
		return name;
	}

	/**
	 * convert the priority flag in a string describing the priority through a
	 * set of constant from ISources<br>
	 * If there is an unknown flag value it is reported at the end of the string
	 * as its int value
	 * 
	 * @return a String representation of the priority flag
	 */
	public String getPriority() {
		String result = "";
		int p = priority;
		for (int i = 0; i < PRIORITIES.length; i++) {
			if ((p & PRIORITIES[i]) == PRIORITIES[i]) {
				result += "|" + PRIORITY_NAMES[i];
				p ^= PRIORITIES[i]; // remove the flag from p
			}
		}
		if (p != 0) { // there is unknown flag
			result += "|" + p;
		}
		if (result.length() > 1) { // remove the leading |
			result = result.substring(1);
		}
		return result;
	}

	/**
	 * Convert the value to a String<br>
	 * if the value is null, it returns "null" if the value is
	 * IEvaluationContext.UNDEFINED_VARIABLE then it returns
	 * "UNDEFINED_VARIABLE" otherwise it returns value.toString()
	 * 
	 * @return a String representation of the value
	 */
	public String getValue() {
		if (IEvaluationContext.UNDEFINED_VARIABLE.equals(value)) {
			return "UNDEFINED_VARIABLE";
		}
		return value != null ? value.toString() : "null";
	}

}