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
import sgdi.pr2.grupo03.interfaces.ArbolClasificador;
import sgdi.pr2.grupo03.model.Instance;
import sgdi.pr2.grupo03.model.Table;
import sgdi.pr2.grupo03.model.TableInfo;
import sgdi.pr2.grupo03.model.TreeTable;
import sgdi.pr2.grupo03.util.FileUtil;
import sgdi.pr2.grupo03.util.PrintUtil;

//------------------------------------------------------------------------------------------
// Apartado C: Creación de árboles de clasificación utilizando el método TDIDT (opcional, 30%)
//
// Escribir una clase que implemente el método TDIDT para la generación de un árbol de
// clasificación. Utilizaremos las mismas suposiciones que en apartados anteriores: las
// instancias tienen todos los atributos categóricos, los ficheros siguen el formato CSV
// y todos los valores de los atributos aparecen en el conjunto de entrenamiento. Para la
// generación del árbol se debe usar la ganancia de información (o disminución de entropía)
// como criterio de selección de atributos.
//
// Se debe crear una clase llamada TDIDT que implemente la interfaz ArbolClasificador.
//------------------------------------------------------------------------------------------

public class TDIDT implements ArbolClasificador {
    //******************************************************************************************
    // Propiedades:
    //******************************************************************************************

    private Table _table = null;
    private TableInfo _info = null;
    private TreeTable _tree = null;
    //...

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
            if (_tree != null) {
                msg += "\n" + _tree;
            }
        }
        return msg;
    }

    @Override
    public String info() {
        return Practica.info();
    }

    //------------------------------------------------------------------------------------------
    // entrena( String path, String pathArbol ): genera el árbol de clasificación y muestra
    // un resumen similar al apartado B:
    // • Número total de instancias leídas
    // • Número total de instancias mal formadas
    // • Para cada atributo una lista de sus posibles valores, indicando de manera especial
    //   el atributo clase
    //
    // Además de producir el árbol de clasificación en el fichero y almacenarlo internamente
    // para posteriormente clasificar, cuando se llame al procedimiento recursivo que crea el
    // árbol (la función tdidt de los apuntes) se debe mostrar por pantalla:
    // • El subconjunto del conjunto de entrenamiento que se está considerando, mostrando
    //   para ello los atributos que ya han sido establecidos a un valor. Al tratar el conjunto
    //   inicial no se mostraría nada (ningún atributo ha sido establecido a un valor), pero
    //   en la llamada recursiva que crea el árbol para el subconjunto cuyo atributo a toma
    //   el valor v, se mostraría un mensaje del estilo: “Generando árbol para subconjunto
    //   a=v”. Para subconjuntos más pequeños resultantes de una secuencia de particionados
    //   se incluiría la secuencia completa: “Generando árbol para subconjunto a1=v1, a2=v2,
    //   …, an=vn”.
    // • La ganancia de información y la entropía media resultante de particionar por cada
    //   uno de los atributos candidatos.
    // • Un mensaje indicando cuál ha sido el atributo que finalmente se ha elegido
    //   para ramificar.
    //------------------------------------------------------------------------------------------
    @Override
    public void entrena(String path, String dotTreePath) {
        // Cargamos la tabla de instancias de entrenamiento:
        _table = new Table();
        if (_table.load(path)) {
            // Obtenemos la tabla de información sobre los campos:
            _info = _table.getTableInfo(true);
            // Creamos el árbol de particionado y lo salvamos en formato DOT:
            _tree = new TreeTable(_table, _info);
            saveDotTree(dotTreePath);
            // Mostramos algo información al usuario:
            System.out.println("Readed instances:  " + _table.getReadedInstances());
            System.out.println("Invalid instances: " + _table.getInvalidInstances());
            System.out.println("Attributes:");
            PrintUtil.PrintLinesWithTab(_info.toString());
            System.out.println();
        } else {
            _table = null;
            _info = null;
            _tree = null;
        }
    }

    //------------------------------------------------------------------------------------------
    // Además de generar el árbol y almacenarlo internamente para posteriormente clasificar,
    // deberá escribirlo en formato DOT en el fichero pathArbol. DOT es un formato textual
    // muy sencillo para la descripción de grafos, que luego pueden visualizarse con distintas
    // herramientas. Podéis encontrar más información sobre el lenguaje en:
    // • http://en.wikipedia.org/wiki/DOT_%28graph_description_language%29
    // • http://www.graphviz.org/Documentation.php
    //
    // Para visualizar grafos en formato DOT existen varias herramientas (podéis consultar en
    // http://www.graphviz.org/Resources.php) y también existe una interfaz web (Ajax viewer:
    // http://ashitani.jp/gv/ y http://ashitani.jp/wiki/?p=Ajax%2FGraphviz_English para las
    // instrucciones). Un ejemplo muy simple que muestra todos los elementos del lenguaje DOT
    // que necesitáis es:
    //
    //  digraph tree {
    //     //nodos
    //     sexo [label="Sexo",shape="box"];
    //     f1 [label="Futbol"];
    //     exp [label="Expediente",shape="box"];
    //     f2 [label="Futbol"];
    //     p [label="Padel"];
    //     //aristas
    //     sexo -> f1 [label="M"];
    //     sexo -> exp [label="F"];
    //     exp -> f2 [label="<A"];
    //     exp -> p [label=">=A"];
    //  }
    //
    // NOTA: el visor on-line AJAX no funciona muy bien con algunas características como
    // los comentarios (líneas que comienzan con // ), los nodos con formas diferentes de
    // la estándar (shape=”box”) o las tildes en las etiquetas (label=”Fútbol”). Además,
    // no necesita que los elementos aparezcan dentro del entorno digraph (digraph NAME
    // { …. }) para renderizarlo.
    //------------------------------------------------------------------------------------------
    private void saveDotTree(String dotTreePath) {
        List<String> lines = _tree.toDotString();
        FileUtil.setLines(dotTreePath, lines);
    }

    //------------------------------------------------------------------------------------------
    // String clasifica( String instancia ): dada una cadena de texto conteniendo los atributos
    // de una instancia en formato CSV, devuelve una cadena de texto con la clase que predice
    // el clasificador. Si la instancia tiene un número de atributos diferente al de las
    // instancias con las que se entrenó el clasificador, se debe mostrar un mensaje por
    // consola con el error y el contenido de la instancia errónea.
    //------------------------------------------------------------------------------------------
    @Override
    public String clasifica(String instance) {
        Instance victim = new Instance(instance);
        if (_table.getHeader().sameNumberOfFields(victim)) {
            return _tree.calculateClass(victim);
        } else {
            System.err.println("Invalid type of instance!");
        }
        return null;
    }

    //------------------------------------------------------------------------------------------
    // double test( String path ), donde path es la ubicación del fichero de test . Para cada
    // instancia leída del fichero se mostrará:
    // • Valores de los atributos de la instancia completa, incluida la clase
    // • Clase que predice el árbol de clasificación
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
                    // Calculamos la clase y guardamos la clase que realmente es:
                    String classResult = clasifica(line);
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
