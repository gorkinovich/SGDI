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

import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class FileUtil {
	// ******************************************************************************************
	// Constructores:
	// ******************************************************************************************

	private FileUtil() {
	}

	// ******************************************************************************************
	// Métodos:
	// ******************************************************************************************

	public static List<String> getLines(String path) {
		try {
			Path filePath = FileSystems.getDefault().getPath(path);
			return Files.readAllLines(filePath, Charset.defaultCharset());
		} catch (Exception e) {
			System.out.printf(e.getMessage());
			return null;
		}
	}

	public static void setLines(String path, List<String> lines) {
		try {
			Path filePath = FileSystems.getDefault().getPath(path);
			Files.write(filePath, lines, Charset.defaultCharset());
		} catch (Exception e) {
			System.out.printf(e.getMessage());
		}
	}
}
