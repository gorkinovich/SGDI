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

import sgdi.pr2.grupo03.interfaces.ClasificadorkNN;
import sgdi.pr2.grupo03.model.Instance;
import sgdi.pr2.grupo03.model.Table;
import sgdi.pr2.grupo03.model.TableInfo;
import sgdi.pr2.grupo03.util.FileUtil;
import sgdi.pr2.grupo03.util.ListUtil;

//------------------------------------------------------------------------------------------
// Apartado A: Clasificador kNN (obligatoria, 30%)
//
// Se debe crear una clase llamada kNN que implemente la interfaz ClasificadorkNN.
//
// En este apartado supondremos que los atributos de todas las instancias son continuos.
// No es necesario aplicar normalización ni ponderación a los atributos.
//------------------------------------------------------------------------------------------

public class kNN implements ClasificadorkNN {
    //******************************************************************************************
    // Propiedades:
    //******************************************************************************************

    private Table _table = null;
    private TableInfo _info = null;

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
        }
        return msg;
    }

    @Override
    public String info() {
        return Practica.info();
    }

    //------------------------------------------------------------------------------------------
    // void entrena( String path ), donde path es la ubicación del fichero de entrenamiento.
    // Este método lee el fichero y entrena el algoritmo con las instancias leídas. El fichero
    // tiene formato CSV donde la primera línea contiene el nombre de los atributos y la clase.
    // El último atributo será siempre la clase. Todas las líneas deben tener el mismo número
    // de campos que la primera. Si alguna no lo cumple se debe ignorar y mostrar un mensaje
    // por consola con el número de línea y su contenido.
    //------------------------------------------------------------------------------------------
    @Override
    public void entrena(String path) {
        // Cargamos la tabla de instancias de entrenamiento:
        _table = new Table();
        if (_table.load(path)) {
            // Obtenemos la tabla de información sobre los campos:
            _info = _table.getTableInfo(false);
        } else {
            _table = null;
            _info = null;
        }
    }

    //------------------------------------------------------------------------------------------
    // String clasifica( String instancia, int k ): dada una cadena de texto en formato
    // CSV conteniendo los atributos de una instancia (excepto la clase) y un valor de k,
    // devuelve una cadena de texto con la clase que predice el clasificador. Si la instancia
    // tiene un número de atributos diferente al de las instancias con las que se entrenó el
    // clasificador entonces mostrará un mensaje por consola indicando el error y el contenido
    // de la instancia errónea.
    //------------------------------------------------------------------------------------------
    @Override
    public String clasifica(String instance, int k) {
        Instance victim = new Instance(instance);
        if (_table.getHeader().sameNumberOfFields(victim)) {
            // Inicializar el algoritmo:
            float[] distance = new float[k];
            Instance[] neighbour = new Instance[k];
            for (int i = 0; i < k; i++) {
                distance[i] = Float.NaN;
                neighbour[i] = null;
            }

            // Para cada linea cargada en el entrenamiento:
            int numberOfFields = _table.getHeader().numberOfFields();
            for (Instance item : _table.getValues()) {
            //_values.stream().forEach((item) -> {
                // Buscamos la distancia:
                float delta = getEuclideanDistance(numberOfFields - 1, victim, item);
                // La añadimos a la tabla de los más cercanos:
                for (int i = 0; i < k; ++i) {
                    if (neighbour[i] == null) {
                        // La posición en la tabla está vacía:
                        distance[i] = delta;
                        neighbour[i] = item;
                    } else if (delta < distance[i]) {
                        // El elemento está más cerca que el de la tabla:
                        for (int j = k - 1; j > i; --j) {
                            distance[j] = distance[j - 1];
                            neighbour[j] = neighbour[j - 1];
                        }
                        distance[i] = delta;
                        neighbour[i] = item;
                    }
                }
            //});
            }

            // Hacemos un conteo de las clases de las instancias:
            Map<String, Integer> countClasses = ListUtil.countClasses(neighbour);

            // Buscamos el elemento que más veces aparece:
            Map.Entry<String, Integer> maximum = ListUtil.getMaximum(countClasses);
            if (maximum == null) {
                System.err.println("Can't find the mode!");
            } else {
                return maximum.getKey();
            }
        } else {
            System.err.println("Invalid type of instance!");
        }
        return null;
    }

    private float getEuclideanDistance(int length, Instance left, Instance right) {
        float delta = 0.0f;
        for (int i = 0; i < length; ++i) {
            float val = getDelta(left, right, i);
            delta += (val * val);
        }
        return (float)Math.sqrt(delta);
    }

    private float getDelta(Instance left, Instance right, int index) {
        float lval = _info.getWeight(left, index);
        float rval = _info.getWeight(right, index);
        if (Float.isNaN(lval) || Float.isNaN(rval)) {
            return left.getField(index).compareTo(right.getField(index)) == 0 ? 0.0f : 1.0f;
        }
        return Math.abs(lval - rval);
    }

    //------------------------------------------------------------------------------------------
    // double test( String path, int k ), donde path es la ubicación del fichero de test.
    // Para cada instancia leída del fichero se mostrará:
    // • Valores de los atributos de la instancia completa, incluida la clase
    // • Clase que predice el clasificador kNN usando el valor k.
    // • Una linea indicando si acierta o falla al clasificar
    // Al terminar de procesar todas las instancias de test devolverá el índice de acierto
    // y mostrará por pantalla un resumen:
    // • Número total de instancias leídas
    // • Número total de instancias mal formadas
    // • Índice de acierto: “instancias correctamente predichas” / “instancias de test
    //   totales (bien formadas)”.
    //------------------------------------------------------------------------------------------
    @Override
    public double test(String path, int k) {
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
                    // Calculamos la clase y guardamos la clase que realmente es:
                    String classResult = clasifica(line, k);
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
