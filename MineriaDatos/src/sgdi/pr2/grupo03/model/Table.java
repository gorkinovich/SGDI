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
import java.util.List;
import sgdi.pr2.grupo03.util.FileUtil;

public class Table {
    //******************************************************************************************
    // Propiedades:
    //******************************************************************************************

    private Instance _header;
    private List<Instance> _values;

    private int _readedInstances;
    private int _invalidInstances;

    //******************************************************************************************
    // Constructores:
    //******************************************************************************************

    public Table() {
        _header = null;
        _values = null;
    }

    //******************************************************************************************
    // Métodos:
    //******************************************************************************************

    public boolean load(String path) {
        return load(path, true);
    }

    public boolean load(String path, boolean showErrors) {
        // Cargamos todas las líneas del fichero indicado:
        List<String> lines = FileUtil.getLines(path);
        if (lines != null) {
            // Obtenemos la cabecera y reservamos un array de elementos:
            int len = lines.size();
            _header = new Instance(lines.get(0));
            _values = new ArrayList<>(len - 1);
            _invalidInstances = 0;
            // Para cada línea del fichero creamos una instancia de datos:
            for (int i = 1; i < len; ++i) {
                String line = lines.get(i);
                Instance victim = new Instance(line);
                // Solo añadimos la instancia si cumple el tamaño de la cabecera:
                if (_header.sameNumberOfFields(victim)) {
                    _values.add(victim);
                } else {
                    ++_invalidInstances;
                    if (showErrors) {
                        System.err.println("Instance ignored (line " + i + "): " + line);
                    }
                }
            }
            _readedInstances = len - 1;
            return true;
        } else {
            // No había líneas que leer, así que no hay datos que procesar:
            _header = null;
            _values = null;
            _readedInstances = 0;
            _invalidInstances = 0;
            return false;
        }
    }

    public TableInfo getTableInfo(boolean fullInfo) {
        return new TableInfo(_header, _values, fullInfo);
    }

    public Instance getHeader() {
        return _header;
    }

    public List<Instance> getValues() {
        return _values;
    }

    public int getReadedInstances() {
        return _readedInstances;
    }

    public int getInvalidInstances() {
        return _invalidInstances;
    }

    @Override
    public String toString() {
        String msg = "";
        if (_header != null) {
            msg += _header.toString() + "\n";
        }
        if (_values != null) {
            //msg = _values.stream().map((item) -> item.toString() + "\n")
            //                      .reduce(msg, String::concat);
            for (Instance item : _values) {
                msg += item.toString() + "\n";
            }
        }
        return msg;
    }
}
