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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TableInfo {
    //******************************************************************************************
    // Propiedades:
    //******************************************************************************************

    private final Instance _header;
    private final FieldInfo[] _fields;

    //******************************************************************************************
    // Constructores:
    //******************************************************************************************

    public TableInfo(Instance header, List<Instance> values, boolean fullInfo) {
        // Obtenemos la cabecera y reservamos un array de elementos:
        _header = header;
        _fields = new FieldInfo[_header.numberOfFields()];
        // Para cada campo vamos a comprobar el tipo:
        for (int i = 0; i < _fields.length; ++i) {
            if (checkFieldIsInt(values, i)) {
                _fields[i] = new FieldInfo(FieldType.intField,
                                           fullInfo ? findSetOfValues(values, i) : null);
            } else if (checkFieldIsFloat(values, i)) {
                _fields[i] = new FieldInfo(FieldType.floatField,
                                           fullInfo ? findSetOfValues(values, i) : null);
            } else {
                _fields[i] = new FieldInfo(FieldType.stringField,
                                           findSetOfValues(values, i));
            }
        }
    }

    //******************************************************************************************
    // Métodos:
    //******************************************************************************************

    public float getWeight(Instance victim, int index) {
        switch (_fields[index].getType()) {
            case intField:
                return victim.getFieldAsInt(index);

            case floatField:
                return victim.getFieldAsFloat(index);

            default:
                String val = victim.getField(index);
                if (val.compareTo("vlow") == 0) {
                    return 0.0f;
                } else if (val.compareTo("low") == 0 || val.compareTo("small") == 0) {
                    return 1.0f;
                } else if (val.compareTo("med") == 0) {
                    return 2.0f;
                } else if (val.compareTo("high") == 0 || val.compareTo("big") == 0) {
                    return 3.0f;
                } else if (val.compareTo("vhigh") == 0) {
                    return 4.0f;
                } else if (val.compareTo("2") == 0) {
                    return 2.0f;
                } else if (val.compareTo("3") == 0) {
                    return 3.0f;
                } else if (val.compareTo("4") == 0) {
                    return 4.0f;
                } else if (val.compareTo("5more") == 0 || val.compareTo("more") == 0) {
                    return 5.0f;
                }
                return Float.NaN;
        }
    }

    public Instance getHeader() {
        return _header;
    }

    public FieldInfo[] getFields() {
        return _fields;
    }

    @Override
    public String toString() {
        String msg = "";
        if (_header != null && _fields != null) {
            int last = _fields.length - 1;
            for (int i = 0; i < last; i++) {
                msg += _header.getField(i) + " " + _fields[i] + "\n";
            }
            msg += "[" + _header.getField(last) + "] " + _fields[last];
        }
        return msg;
    }

    //******************************************************************************************
    // Métodos (static):
    //******************************************************************************************

    public static Set<String> findSetOfValues(List<Instance> values, int index) {
        Set<String> fieldValues = new HashSet<>();
        for (Instance item : values) {
            fieldValues.add(item.getField(index));
        }
        return fieldValues;
    }

    public static boolean checkFieldIsInt(List<Instance> values, int index) {
        // Comprueba que todos los campos de un índice son enteros:
        for (Instance item : values) {
            if (!item.isFieldInt(index))
                return false;
        }
        return true;
        //return _values.stream().allMatch((item) -> item.isFieldInt(index));
    }

    public static boolean checkFieldIsFloat(List<Instance> values, int index) {
        // Comprueba que todos los campos de un índice son reales:
        for (Instance item : values) {
            if (!item.isFieldFloat(index))
                return false;
        }
        return true;
        //return _values.stream().allMatch((item) -> item.isFieldFloat(index));
    }
}
