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
import java.util.Arrays;
import java.util.List;

public class Instance {
    //******************************************************************************************
    // Propiedades:
    //******************************************************************************************

	private List<String> _values;

    //******************************************************************************************
    // Constructores:
    //******************************************************************************************

    public Instance(String values) {
        initialize(values, ",");
	}

    public Instance(String values, String separator) {
        initialize(values, separator);
	}

	public Instance(List<String> values) {
		_values = values;
	}

    //******************************************************************************************
    // Métodos:
    //******************************************************************************************

    private void initialize(String values, String separator) {
		_values = new ArrayList<>();
        _values.addAll(Arrays.asList(values.split(separator)));
    }

    public int numberOfFields() {
        return _values.size();
    }

    public boolean sameNumberOfFields(Instance victim) {
        return numberOfFields() == victim.numberOfFields();
    }

    public String getField(int index) {
        return _values.get(index);
    }

    public int getFieldAsInt(int index) {
        return Integer.parseInt(_values.get(index));
    }

    public float getFieldAsFloat(int index) {
        return Float.parseFloat(_values.get(index));
    }

    public boolean isFieldInt(int index) {
        try {
            getFieldAsInt(index);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFieldFloat(int index) {
        try {
            getFieldAsFloat(index);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getLastField() {
        return _values.get(_values.size() - 1);
    }

    public List<String> getValues() {
        return _values;
    }

    @Override
    public String toString() {
        String msg = null;
        for (String item : _values) {
            if (msg == null) {
                msg = item;
            } else {
                msg += (", " + item);
            }
        }
        return msg;
    }
}
