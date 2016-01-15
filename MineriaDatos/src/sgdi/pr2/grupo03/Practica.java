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

public class Practica {
    //******************************************************************************************
    // Entrada principal del programa:
    //******************************************************************************************
    public static void main(String[] args) {
    	knn();
        bayesian();
        tdidt();
    }

    //******************************************************************************************
    // Prueba apartado A:
    //******************************************************************************************
    private static void knn() {
        System.out.println("******************");
        System.out.println("* KNN (car.data) *");
        System.out.println("******************\n");
        knn("car-training.data", "car-test.data");

        System.out.println("*******************");
        System.out.println("* KNN (iris.data) *");
        System.out.println("*******************\n");
        knn("iris-training.data", "iris-test.data");
    }

    private static void knn(String training, String test) {
    	kNN algorithm = new kNN();
        algorithm.entrena(training);
        //System.out.println(algorithm + "\n");
        algorithm.test(test, 5);
        System.out.println("\n");
    }

    //******************************************************************************************
    // Prueba apartado B:
    //******************************************************************************************
    private static void bayesian() {
        System.out.println("************************");
        System.out.println("* Bayesian (car.data) *");
        System.out.println("************************");
        bayesian("car-training.data", "car-test.data");

        System.out.println("************************");
        System.out.println("* Bayesian (iris.data) *");
        System.out.println("************************");
        bayesian("iris-training.data", "iris-test.data");
    }

    private static void bayesian(String training, String test) {
        BayesianoSimple algorithm = new BayesianoSimple();
        algorithm.entrena(training);
        //System.out.println(algorithm + "\n");
        algorithm.test(test);
        System.out.println("\n");
    }

    //******************************************************************************************
    // Prueba apartado C:
    //******************************************************************************************
    private static void tdidt() {
        System.out.println("********************");
        System.out.println("* TDIDT (car.data) *");
        System.out.println("********************\n");
        tdidt("car-training.data", "car-test.data", "car.dot");

        System.out.println("*********************");
        System.out.println("* TDIDT (iris.data) *");
        System.out.println("*********************\n");
        tdidt("iris-training.data", "iris-test.data", "iris.dot");
    }

    private static void tdidt(String training, String test, String dotTree) {
        TDIDT algorithm = new TDIDT();
        algorithm.entrena(training, dotTree);
        //System.out.println(algorithm + "\n");
        algorithm.test(test);
        System.out.println("\n");
    }

    //******************************************************************************************
    // Información sobre la práctica:
    //******************************************************************************************
    public static String info() {
        return "Asignatura: SGDI\n" +
               "Práctica:   2\n"+
               "Autores:    Rotaru, Dan Cristian\n" +
               "            Suárez García, Gorka\n\n" +
               "Ámbos dos declaramos que el código del proyecto " +
               "es fruto exclusivamente del trabajo de sus miembros, " +
               "a excepción del código aportado por el profesor.";
    }
}
