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
import java.util.Set;
import sgdi.pr2.grupo03.util.ListUtil;
import sgdi.pr2.grupo03.util.PrintUtil;

public class BayesianTable {
    //******************************************************************************************
    // Tipos:
    //******************************************************************************************

    private class BayesianValue {
        public String name = null;
        public float ratio = 0.0f;
    }

    private class BayesianAttribute {
        public String name = null;
        public List<BayesianValue> values = null;
        public BayesianValue getValue(String name) {
            for (BayesianValue value : values) {
                if (value.name.compareTo(name) == 0) {
                    return value;
                }
            }
            return null;
        }
    }

    private class BayesianClass {
        public String name = null;
        public float ratio = Float.NaN;
        public List<BayesianAttribute> attributes = null;
    }

    //******************************************************************************************
    // Propiedades:
    //******************************************************************************************

    private List<BayesianClass> _classes = null;

    //******************************************************************************************
    // Constructores:
    //******************************************************************************************

    public BayesianTable(Table table, TableInfo info) {
        createClasses(table, info);
        calculateRatios(table);
    }

    //******************************************************************************************
    // Métodos:
    //******************************************************************************************

    private void createClasses(Table table, TableInfo info) {
        Instance header = table.getHeader();
        List<Instance> tableValues = table.getValues();
        FieldInfo[] fieldsInfo = info.getFields();
        int last = fieldsInfo.length - 1;
        _classes = new ArrayList<>();
        // Por cada clase vamos a crear un elemento en la tabla:
        for (String className : fieldsInfo[last].getValues()) {
            BayesianClass classVictim = new BayesianClass();
            classVictim.name = className;
            classVictim.attributes = new ArrayList<>();
            // Por cada atributo vamos a crear un subelemento:
            for (int i = 0; i < last; i++) {
                BayesianAttribute attribVictim = new BayesianAttribute();
                attribVictim.name = header.getField(i);
                attribVictim.values = new ArrayList<>();
                // Por cada valor vamos a crear un subelemento:
                Set<String> values = fieldsInfo[i].getValues();
                if (values == null) {
                    values = TableInfo.findSetOfValues(tableValues, i);
                }
                for (String value : values) {
                    BayesianValue valueVictim = new BayesianValue();
                    valueVictim.name = value;
                    attribVictim.values.add(valueVictim);
                }
                classVictim.attributes.add(attribVictim);
            }
            _classes.add(classVictim);
        }
    }

    private void calculateRatios(Table table) {
        // Para cada tipo de clase vamos a calcular las probabilidades:
        int last = table.getHeader().numberOfFields() - 1;
        List<Instance> tableValues = table.getValues();
        for (BayesianClass item : _classes) {
            // Cogemos las instancias que tienen la misma clase:
            List<Instance> sublist = new ArrayList<>();
            for (Instance instance : tableValues) {
                if (instance.getField(last).compareTo(item.name) == 0) {
                    sublist.add(instance);
                }
            }
            // Calculamos la probabilidad de la clase:
            item.ratio = (float)sublist.size() / (float)tableValues.size();
            // Recorremos cada atributo para calcular la probabilidad condicional:
            for (int i = 0; i < last; i++) {
                // Buscamos cuantas veces aparece cada valor en el atributo:
                Map<String, Integer> count = ListUtil.countValues(sublist, i);
                // Por cada valor calculamos un porcentaje condicional:
                BayesianAttribute attribute = item.attributes.get(i);
                for (Map.Entry<String, Integer> entry : count.entrySet()) {
                    BayesianValue value = attribute.getValue(entry.getKey());
                    value.ratio = entry.getValue().floatValue() / (float)sublist.size();
                }
            }
        }
    }

    public String calculateClass(Instance instance) {
        if (_classes != null && _classes.size() > 0) {
            // Preparamos la busqueda de la clase más probable:
            float maximum = Float.MIN_VALUE;
            String result = null;
            // Para cada clase vamos a comprobar qué probabilidad obtenemos:
            for (BayesianClass citem : _classes) {
                // Añadimos la probabilidad de la clase:
                float ratio = citem.ratio;
                // Recorremos todos los atributos de la instancia:
                int last = citem.attributes.size() - 1;
                for (int i = 0; i < last; i++) {
                    // Obtenemos el valor actual que hay en la instancia:
                    BayesianAttribute aitem = citem.attributes.get(i);
                    BayesianValue vitem = aitem.getValue(instance.getField(i));
                    // Añadimos la probabilidad de dicho valor encontrado:
                    if (vitem != null) {
                        ratio *= vitem.ratio;
                    } else {
                        ratio = 0.0f;
                    }
                }
                // Comprobamos si tenemos un nuevo máximo:
                if (ratio > maximum) {
                    maximum = ratio;
                    result = citem.name;
                }
            }
            return result;
        }
        return null;
    }

    public Map<String, Float> calculateRatios(Instance instance) {
        if (_classes != null && _classes.size() > 0) {
            // Para cada clase vamos a comprobar qué probabilidad obtenemos:
            Map<String, Float> values = new HashMap<>();
            for (BayesianClass citem : _classes) {
                // Añadimos la probabilidad de la clase:
                float ratio = citem.ratio;
                // Recorremos todos los atributos de la instancia:
                int last = citem.attributes.size() - 1;
                for (int i = 0; i < last; i++) {
                    // Obtenemos el valor actual que hay en la instancia:
                    BayesianAttribute aitem = citem.attributes.get(i);
                    BayesianValue vitem = aitem.getValue(instance.getField(i));
                    // Añadimos la probabilidad de dicho valor encontrado:
                    if (vitem != null) {
                        ratio *= vitem.ratio;
                    } else {
                        ratio = 0.0f;
                    }
                }
                // Añadimos el valor obtenido a la tabla:
                values.put(citem.name, ratio);
            }
            return values;
        }
        return null;
    }

    @Override
    public String toString() {
		final String TAB = PrintUtil.TAB;
        String msg = "";
        for (BayesianClass citem : _classes) {
            msg += citem.name + ": " + citem.ratio + "\n";
            for (BayesianAttribute aitem : citem.attributes) {
                msg += TAB + aitem.name + ": {";
                String submsg = null;
                for (BayesianValue vitem : aitem.values) {
                    if (submsg == null) {
                        submsg = "";
                    } else {
                        submsg += ", ";
                    }
                    submsg += vitem.name + ": " + vitem.ratio;
                }
                msg += (submsg != null ? submsg : "") + "}\n";
            }
        }
        return msg.substring(0, msg.length() - 1);
    }
}
