//------------------------------------------------------------------------------------------
// SGDI, Práctica 2, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad:
// Ámbos dos declaramos que el código del proyecto es fruto exclusivamente
// del trabajo de sus miembros, a excepción del código aportado por el profesor.
//------------------------------------------------------------------------------------------
package sgdi.pr2.grupo03.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sgdi.pr2.grupo03.util.ListUtil;
import sgdi.pr2.grupo03.util.PrintUtil;

public class TreeTable {
	// ******************************************************************************************
	// Tipos:
	// ******************************************************************************************

	private class Node {
		public int index = 0;
		public String attribute = null;
		public String classValue = null;
		public HashMap<String, Node> childs = null;
	}

	private class Subtable {
		// public int index = 0;
		public String value = null;
		public List<Instance> table = null;
	}

	private class DotParams {
		public int nid = 0;
	}

	// ******************************************************************************************
	// Propiedades:
	// ******************************************************************************************

	private int _last = 0;
	private Node _root = null;

	// ******************************************************************************************
	// Constructores:
	// ******************************************************************************************

	public TreeTable(Table table, TableInfo info) {
		_last = table.getHeader().numberOfFields() - 1;
		_root = makeTree(table, info);
	}

	// ******************************************************************************************
	// Métodos (Entrenar):
	// ******************************************************************************************

	private Node makeTree(Table table, TableInfo info) {
		boolean[] notUsedAttribute = new boolean[_last];
		for (int i = 0; i < _last; i++) {
			notUsedAttribute[i] = true;
		}
		return makeTree(table.getValues(), table.getHeader(), info,
				notUsedAttribute, null);
	}

	private Node makeTree(List<Instance> values, Instance header,
			TableInfo info, boolean[] notUsedAttribute, String outMsg) {
		// Hacer un conteo de las clases que hay en la tabla:
		Map<String, Integer> countClasses = ListUtil.countClasses(values);
		Map.Entry<String, Integer> maximum = ListUtil.getMaximum(countClasses);
		if (maximum == null)
			return null;

		// Si solo hay una clase no hace falta hacer la partición:
		if (maximum.getValue() == values.size()) {
			return makeFinalNode(maximum, header);
		}

		// Preparamos el algoritmo para dividir la tabla en un árbol:
		float size = values.size();
		float startEntropy = calculateEntropy(values);
		float averageEntropy = 0.0f;
		float infoGain = 0.0f;
		List<Subtable> subtables = null;
		int index = 0;

		// Para cada atributo no usado de la tabla vamos a comprobar la ganancia
		// de
		// información, para seleccionar la que nos dé el máximo valor:
		for (int i = 0; i < _last; i++) {
			if (notUsedAttribute[i]) {
				// Partimos la tabla por el atributo seleccionado:
				List<Subtable> candidates = partition(i, values);
				// Calculamos la entropía actual tras partir la tabla:
				float currentEntropy = 0.0f;
				for (Subtable item : candidates) {
					float itemEntropy = calculateEntropy(item.table);
					currentEntropy += ((float) item.table.size() / size)
							* itemEntropy;
				}
				// Comprobamos que tenemos una mayor ganancia de información:
				float currentGain = startEntropy - currentEntropy;
				if (currentGain > infoGain) {
					averageEntropy = currentEntropy;
					infoGain = currentGain;
					subtables = candidates;
					index = i;
				}
			}
		}

		// Si tenemos una lista de subtablas vamos a construir el nodo:
		if (subtables != null) {
			// Preparamos el nodo que va a ser devuelto:
			String nextOutMsg;
			Node result = new Node();
			result.index = index;
			result.attribute = header.getField(index);
			result.classValue = maximum.getKey();
			result.childs = new HashMap<>();
			// Mostramos información requerida por el usuario:
			if (outMsg != null) {
				System.out.println();
			}
			System.out.println("Selected attribute: " + result.attribute);
			System.out.println("Information gain:   " + infoGain);
			System.out.println("Average entropy:    " + averageEntropy);
			System.out.println();
			// Para cada valor posible en el atributo vamos a generar una rama:
			for (String attribValue : info.getFields()[index].getValues()) {
				// Mostramos un aviso informativo por pantalla:
				if (outMsg == null) {
					nextOutMsg = "Generating tree to subset: ";
				} else {
					nextOutMsg = outMsg + ", ";
				}
				nextOutMsg += result.attribute + "=" + attribValue;
				System.out.println(nextOutMsg);
				// Buscamos una subtabla que sea del mismo valor:
				Node child = null;
				for (Subtable subtable : subtables) {
					if (subtable.value.compareTo(attribValue) == 0) {
						notUsedAttribute[index] = false;
						child = makeTree(subtable.table, header, info,
								notUsedAttribute, nextOutMsg);
						notUsedAttribute[index] = true;
						break;
					}
				}
				// Si no había ninguna subtabla generamos un nodo final:
				if (child == null) {
					child = makeFinalNode(maximum, header);
				}
				// Añadimos a los hijos del nodo el subnodo creado:
				result.childs.put(attribValue, child);
			}
			if (outMsg == null) {
				System.out.println();
			}
			return result;
		}
		return null;
	}

	private Node makeFinalNode(Map.Entry<String, Integer> value, Instance header) {
		return makeFinalNode(value.getKey(), header);
	}

	private Node makeFinalNode(String value, Instance header) {
		Node result = new Node();
		result.index = _last;
		result.attribute = header.getLastField();
		result.classValue = value;
		return result;
	}

	private List<Subtable> partition(int index, List<Instance> table) {
		// Partimos la tabla por los valores que encontramos en el atributo
		// indicado:
		HashMap<String, Subtable> subtables = new HashMap<>();
		for (Instance item : table) {
			String value = item.getField(index);
			if (subtables.containsKey(value)) {
				Subtable victim = subtables.get(value);
				victim.table.add(item);
			} else {
				Subtable victim = new Subtable();
				// victim.index = index;
				victim.value = value;
				victim.table = new ArrayList<>();
				victim.table.add(item);
				subtables.put(value, victim);
			}
		}
		// Devulvemos una lista de subtablas:
		return new ArrayList<>(subtables.values());
	}

	private float calculateEntropy(List<Instance> table) {
		// Hacer un conteo de las clases que hay en la tabla:
		Map<String, Integer> countClasses = ListUtil.countClasses(table);
		// Calculamos la entropía que tenemos en nuestro conjunto de datos:
		float entropy = 0.0f;
		float size = table.size();
		for (Map.Entry<String, Integer> item : countClasses.entrySet()) {
			float ratio = item.getValue().floatValue() / size;
			entropy += (-(ratio * (Math.log(ratio) / Math.log(2))));
		}
		return entropy;
	}

	// ******************************************************************************************
	// Métodos (Clasificación):
	// ******************************************************************************************

	public String calculateClass(Instance instance) {
		if (_root != null) {
			return calculateClass(instance, _root);
		}
		return null;
	}

	private String calculateClass(Instance instance, Node node) {
		if (node.index == _last) {
			return node.classValue;
		} else {
			String value = instance.getField(node.index);
			for (Map.Entry<String, Node> child : node.childs.entrySet()) {
				if (child.getKey().compareTo(value) == 0) {
					return calculateClass(instance, child.getValue());
				}
			}
			return node.classValue;
		}
	}

	// ******************************************************************************************
	// Métodos (DOT):
	// ******************************************************************************************

	public List<String> toDotString() {
		List<String> lines = new ArrayList<>();
		List<String> nodes = new ArrayList<>();
		List<String> edges = new ArrayList<>();
		toDotString(_root, nodes, edges, new DotParams());
		lines.add("digraph tree {");
		for (String node : nodes) {
			lines.add(PrintUtil.TAB + node);
		}
		for (String edge : edges) {
			lines.add(PrintUtil.TAB + edge);
		}
		lines.add("}");
		return lines;
	}

	private String toDotString(Node victim, List<String> nodes,
			List<String> edges, DotParams params) {
		// Primero añadimos el nodo:
		String nidName = "n" + params.nid;
		if (victim.index == _last) {
			nodes.add(nidName + " [label=\"" + victim.classValue + "\"]");
		} else {
			nodes.add(nidName + " [label=\"" + victim.attribute
					+ "\",shape=\"box\"]");
		}
		++(params.nid);
		// Segundo añadimos las aristas:
		if (victim.childs != null) {
			for (Map.Entry<String, Node> child : victim.childs.entrySet()) {
				String nextName = toDotString(child.getValue(), nodes, edges,
						params);
				edges.add(nidName + " -> " + nextName + "[label=\""
						+ child.getKey() + "\"]");
			}
		}
		return nidName;
	}

	// ******************************************************************************************
	// Métodos (toString):
	// ******************************************************************************************

	@Override
	public String toString() {
		return toString(_root, "");
	}

	private String toString(Node victim, String tab) {
		String msg = victim.attribute + "(" + victim.index + ") -> "
				+ victim.classValue + "\n";
		if (victim.childs != null) {
			for (Map.Entry<String, Node> child : victim.childs.entrySet()) {
				msg += tab + child.getKey() + " => "
						+ toString(child.getValue(), tab + PrintUtil.TAB);
			}
		}
		return msg;
	}
}
