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

import java.util.Set;

public class FieldInfo {
    //******************************************************************************************
    // Propiedades:
    //******************************************************************************************

    private final FieldType _type;
    private final Set<String> _values;

    //******************************************************************************************
    // Constructores:
    //******************************************************************************************

    public FieldInfo(FieldType type, Set<String> values) {
        _type = type;
        _values = values;
    }

    //******************************************************************************************
    // Métodos:
    //******************************************************************************************

    public FieldType getType() {
        return _type;
    }

    public Set<String> getValues() {
        return _values;
    }

    @Override
    public String toString() {
        String msg = "(";
        switch (_type) {
            case intField:   msg += "integer"; break;
            case floatField: msg += "float";   break;
            default:         msg += "string";  break;
        }
        msg += ")";
        if (_values != null) {
            msg += " ->";
            for (String item : _values) {
                msg += " " + item;
            }
        }
        return msg;
    }
}
