//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion2.menus.movieTheater;

import sgdi.pr3.grupo03.shared.ConsoleUtil;
import sgdi.pr3.grupo03.situacion2.DBHelper;
import sgdi.pr3.grupo03.situacion2.menus.Menu;
import sgdi.pr3.grupo03.situacion2.menus.control.ChooseAction;
import sgdi.pr3.grupo03.situacion2.menus.control.MainMenu;
import sgdi.pr3.grupo03.situacion2.model.MovieTheater;

public class RemoveMovieTheater extends Menu<String> {

	@Override
	public String draw() {
		System.out.println("Inserta el nombre del cine que vas a borrar.");

		return ConsoleUtil.getString();
	}

	@Override
	public void input(String name) {
		MovieTheater movieTheater = DBHelper.getMovieTheaterByName(name);
		if (movieTheater == null) {
			System.out
					.println("No se ha encontrado ningún cine con ese nombre.");
			controller.setMenu(ChooseAction.class, MovieTheater.class);
		} else {
			try {
				if (!DBHelper.removeMovieTheater(movieTheater)) {
					System.out
							.println("El cine no se ha borrado correctamente.");
					controller.setMenu(ChooseAction.class, MovieTheater.class);
					return;
				} else {
					System.out.println("Cine borrado correctamente.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			controller.setMenu(MainMenu.class);
		}
	}

}
