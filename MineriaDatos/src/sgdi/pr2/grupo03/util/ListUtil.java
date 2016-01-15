//------------------------------------------------------------------------------------------
// SGDI, Práctica 2, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad:
// Ámbos dos declaramos que el código del proyecto es fruto exclusivamente
// del trabajo de sus miembros, a excepción del código aportado por el profesor.
//------------------------------------------------------------------------------------------
package sgdi.pr2.grupo03.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sgdi.pr2.grupo03.model.Instance;

public final class ListUtil {
	// ******************************************************************************************
	// Constructores:
	// ******************************************************************************************

	private ListUtil() {
	}

	// ******************************************************************************************
	// Métodos:
	// ******************************************************************************************

	private static void addOne(Map<String, Integer> count, String value) {
		int c = 1;
		Integer amount = count.get(value);
		if (amount != null) {
			c += amount;
		}
		count.put(value, c);
	}

	public static Map.Entry<String, Integer> getMaximum(
			Map<String, Integer> count) {
		Map.Entry<String, Integer> maximum = null;
		for (Map.Entry<String, Integer> item : count.entrySet()) {
			if (maximum == null || item.getValue() > maximum.getValue()) {
				maximum = item;
			}
		}
		return maximum;
	}

	// ******************************************************************************************
	// Métodos (List):
	// ******************************************************************************************

	public static Map<String, Integer> countClasses(List<Instance> table) {
		Map<String, Integer> count = new HashMap<>();
		for (Instance item : table) {
			addOne(count, item.getLastField());
		}
		return count;
	}

	public static Map<String, Integer> countValues(List<Instance> table,
			int index) {
		Map<String, Integer> count = new HashMap<>();
		for (Instance item : table) {
			addOne(count, item.getField(index));
		}
		return count;
	}

	// ******************************************************************************************
	// Métodos (Array):
	// ******************************************************************************************

	public static Map<String, Integer> countClasses(Instance[] table) {
		Map<String, Integer> count = new HashMap<>();
		for (Instance item : table) {
			addOne(count, item.getLastField());
		}
		return count;
	}

	public static Map<String, Integer> countValues(Instance[] table,
			int index) {
		Map<String, Integer> count = new HashMap<>();
		for (Instance item : table) {
			addOne(count, item.getField(index));
		}
		return count;
	}
}
