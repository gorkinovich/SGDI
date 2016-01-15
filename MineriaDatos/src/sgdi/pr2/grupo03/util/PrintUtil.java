//------------------------------------------------------------------------------------------
// SGDI, Práctica 2, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad:
// Ámbos dos declaramos que el código del proyecto es fruto exclusivamente
// del trabajo de sus miembros, a excepción del código aportado por el profesor.
//------------------------------------------------------------------------------------------
package sgdi.pr2.grupo03.util;

public final class PrintUtil {
    //******************************************************************************************
    // Propiedades:
    //******************************************************************************************

    public static final String TAB = "    ";

    //******************************************************************************************
    // Constructores:
    //******************************************************************************************

    private PrintUtil() { }

    //******************************************************************************************
    // Métodos:
    //******************************************************************************************

    public static void PrintLinesWithTab(String text) {
        for (String line : text.split("\n")) {
            System.out.println(TAB + line);
        }
    }
}
