// Establecer el grupo adecuado
package sgdi.pr2.grupo03.interfaces;

public interface ClasificadorkNN {

	/*
	 * Devuelve una cadena de texto en la que se indique la
	 * asignatura, la práctica, los autores y una
	 * declaración de integridad expresando que el código es
	 * fruto exclusivamente del trabajo de sus miembros.
	 */
	public String info();

	/*
	 * Lee el fichero de texto 'path' y entrena el algoritmo con
	 * las instancias leídas. Este método debe crear las estructuras
	 * de datos internas.
	 * El fichero 'path' tiene formato CSV donde la primera línea
	 * contiene el nombre de los atributos y la clase. El último
	 * atributo será siempre la clase.
	 * Todas las líneas deben tener el mismo número de campos que
	 * la primera. Si alguna no lo cumple se debe ignorar y mostrar
	 * un mensaje por consola
	 */
	public void entrena(String path);

	/*
	 * Dado una instancia en formato CSV y un valor de k,
	 * predice la clase.
	 * Si la instancia tiene un número de atributos diferente
	 * al de las instancias con las que se entrenó, se debe
	 * mostrar un mensaje por consola y devolver la cadena
	 * vacía.
	 */
	public String clasifica(String instancia, int k);

	/*
	 * Lee el fichero de test en 'path' y clasifica cada instancia
	 * usando el método 'clasifica' con el valor k dado.
	 * Las instancias del fichero tienen como último atributo su clase.
	 * Devuelve la proporción de instancias correctamente clasificadas,
	 * valor que también se debe mostrar por pantalla.
	 */
	public double test(String path, int k);
}
