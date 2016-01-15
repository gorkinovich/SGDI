//------------------------------------------------------------------------------------------
// SGDI, Práctica 2, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad:
// Ámbos dos declaramos que el código del proyecto es fruto exclusivamente
// del trabajo de sus miembros, a excepción del código aportado por el profesor.
//------------------------------------------------------------------------------------------
package sgdi.pr2.grupo03;

import java.util.List;
import java.util.Map;

import sgdi.pr2.grupo03.interfaces.ClasificadorBayesiano;
import sgdi.pr2.grupo03.model.BayesianTable;
import sgdi.pr2.grupo03.model.Instance;
import sgdi.pr2.grupo03.model.Table;
import sgdi.pr2.grupo03.model.TableInfo;
import sgdi.pr2.grupo03.util.FileUtil;
import sgdi.pr2.grupo03.util.PrintUtil;

//------------------------------------------------------------------------------------------
// Apartado B: Clasificador Bayesiano Simple (obligatoria, 40%)
//
// Se debe crear una clase BayesianoSimple que implemente la interfaz ClasificadorBayesiano.
//
// En este caso consideraremos que todos los atributos son categóricos. Los posibles
// valores para cada atributo (incluido el atributo clase) serán los que aparezcan en el
// conjunto de entrenamiento.
//------------------------------------------------------------------------------------------

public class BayesianoSimple implements ClasificadorBayesiano {
    //******************************************************************************************
    // Propiedades:
    //******************************************************************************************

    private Table _table = null;
    private TableInfo _info = null;
    private BayesianTable _btable = null;

    //******************************************************************************************
    // Métodos:
    //******************************************************************************************

    @Override
    public String toString() {
        String msg = "";
        if (_table != null) {
            msg += _table;
            if (_info != null) {
                msg += "\n" + _info + "\n";
            }
            if (_btable != null) {
                msg += "\n" + _btable;
            }
        }
        return msg;
    }

    @Override
    public String info() {
        return Practica.info();
    }

    //------------------------------------------------------------------------------------------
    // void entrena( String path ): Al igual que antes, leerá el conjunto de entrenamiento
    // completo, calculando y almacenando los distintos valores que luego necesitará para
    // predecir la clase. Además de calcular estos valores, el método entrena mostrará por
    // pantalla un resumen:
    // 1. Número total de instancias leídas
    // 2. Número total de instancias mal formadas
    // 3. Para cada atributo una lista de sus posibles valores, indicando de manera especial
    //    el atributo clase
    // 4. Probabilidad P(ci) para cada valor de clase ci en formato de fracción (X/Y) y también
    //    su resultado como número real en [0,1]
    // 5. Para cada valor v de un atributo a y clase ci, la probabilidad condicional P(a=v|ci)
    //    en formato de fracción (X/Y) y también su resultado como número real en [0,1]
    // El fichero tiene formato CSV y la primera línea contiene el nombre de los atributos y la
    // clase. El último atributo será siempre la clase. Todas las líneas deben tener el mismo
    // número de campos que la primera. Si alguna no lo cumple se debe ignorar y mostrar un
    // mensaje por consola con el número de línea y su contenido. En caso de que se haya
    // leído alguna mal formada, para calcular las probabilidades se utilizará el número de
    // instancias leídas bien formadas.
    //------------------------------------------------------------------------------------------
    @Override
    public void entrena(String path) {
        // Cargamos la tabla de instancias de entrenamiento:
        _table = new Table();
        if (_table.load(path)) {
            // Obtenemos la tabla de información sobre los campos:
            _info = _table.getTableInfo(true);
            // Creamos la tabla bayesiana para almacenar las probabilidades:
            _btable = new BayesianTable(_table, _info);
            // Mostramos algo información al usuario:
            System.out.println();
            System.out.println("Readed instances:  " + _table.getReadedInstances());
            System.out.println("Invalid instances: " + _table.getInvalidInstances());
            System.out.println("Attributes:");
            PrintUtil.PrintLinesWithTab(_info.toString());
            System.out.println("Ratios:");
            PrintUtil.PrintLinesWithTab(_btable.toString());
            System.out.println();
        } else {
            _table = null;
            _info = null;
            _btable = null;
        }
    }

    //------------------------------------------------------------------------------------------
    // String clasifica( String instancia ): dada una cadena de texto conteniendo los atributos
    // de una instancia en formato CSV, devuelve una cadena de texto con la clase que predice el
    // clasificador. Si la instancia tiene un número de atributos diferente al de las instancias
    // con las que se entrenó el clasificador, se debe mostrar un mensaje por consola con el
    // error y el contenido de la instancia errónea.
    //------------------------------------------------------------------------------------------
    @Override
    public String clasifica(String instance) {
        Instance victim = new Instance(instance);
        if (_table.getHeader().sameNumberOfFields(victim)) {
            return _btable.calculateClass(victim);
        } else {
            System.err.println("Invalid type of instance!");
        }
        return null;
    }

    //------------------------------------------------------------------------------------------
    // double test( String path ), donde path es la ubicación del fichero de test. Para cada
    // instancia leída del fichero se mostrará:
    // • Valores de los atributos de la instancia completa, incluida la clase
    // • Valores del producto pi para cada clase ci
    // • Clase que predice el clasificador bayesiano
    // • Una linea indicando si acierta o falla al clasificar
    // Al terminar de procesar todas las instancias de test devolverá el índice de acierto y
    // mostrará por pantalla un resumen:
    // • Número total de instancias leídas
    // • Número total de instancias mal formadas
    // • Índice de acierto: “instancias correctamente predichas” / “instancias de test totales
    //   (bien formadas)”.
    //------------------------------------------------------------------------------------------
    @Override
    public double test(String path) {
        // Preparamos la prueba leyendo todas las líneas del fichero:
        double success = 0.0;
        List<String> lines = FileUtil.getLines(path);
        if (lines != null) {
            // Si se han leído las líneas del fichero preparamos el algoritmo:
            Instance header = _table.getHeader();
            int nof = header.numberOfFields();
            int len = lines.size();
            int invalidInstances = 0, successTests = 0;
            // Para cada línea tenemos que realizar el test:
            for (int i = 1; i < len; ++i) {
                // Miramos que es una instancia válida:
                String line = lines.get(i);
                Instance victim = new Instance(line);
                if (nof == victim.numberOfFields()) {
                    // Guardamos el contenido de la instancia para mostrarla:
                    String msg = null;
                    for (int j = 0; j < nof; ++j) {
                        if (msg == null) {
                            msg = "{";
                        } else {
                            msg += ", ";
                        }
                        msg += header.getField(j) + ": " + victim.getField(j);
                    }
                    // Guardamos los porcentajes obtenidos para cada clase:
                    msg += "} => {";
                    Map<String, Float> classRatios = _btable.calculateRatios(victim);
                    if (classRatios != null) {
                        String submsg = null;
                        for (Map.Entry<String, Float> cratio : classRatios.entrySet()) {
                            if (submsg == null) {
                                submsg = "";
                            } else {
                                submsg += ", ";
                            }
                            submsg += cratio.getKey() + ": " + cratio.getValue();
                        }
                        if (submsg != null) {
                            msg += submsg;
                        }
                    }
                    // Calculamos la clase y guardamos la clase que realmente es:
                    String classResult = _btable.calculateClass(victim);
                    String realClass = victim.getLastField();
                    // Terminamos de guardar la información para mostrar en base al éxito:
                    msg += "} => " + classResult + " -> ";
                    if (classResult != null && realClass.compareTo(classResult) == 0) {
                        msg += "true";
                        ++successTests;
                    } else {
                        msg += "false";
                    }
                    System.out.println(msg);
                } else {
                    ++invalidInstances;
                }
            }
            // Finalmente mostramos unas estadísticas para comprobar la eficacia:
            int readedInstances = len - 1;
            if (readedInstances - invalidInstances != 0) {
                success = (double)successTests / (double)(readedInstances - invalidInstances);
            }
            System.out.println();
            System.out.println("Readed instances:  " + readedInstances);
            System.out.println("Invalid instances: " + invalidInstances);
            System.out.println("Sucess ratio:      " + success);
        } else {
            System.err.println("Can't load the test file!");
        }
        return success;
    }
}
