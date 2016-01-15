//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion2.menus.film;

import sgdi.pr3.grupo03.shared.ConsoleUtil;
import sgdi.pr3.grupo03.situacion2.DBHelper;
import sgdi.pr3.grupo03.situacion2.menus.Menu;
import sgdi.pr3.grupo03.situacion2.menus.control.ChooseAction;
import sgdi.pr3.grupo03.situacion2.menus.control.MainMenu;
import sgdi.pr3.grupo03.situacion2.model.Film;

public class ModifyFilm extends Menu<String> {

	@Override
	public String draw() {
		System.out
				.println("Inserta el nombre de la película que vas a modificar.");

		return ConsoleUtil.getString();
	}

	@Override
	public void input(String filmTitle) {
		Film film = DBHelper.getFilmByTitle(filmTitle);
		if (film == null) {
			System.out
					.println("No se ha encontrado ninguna película con ese título.");
			controller.setMenu(ChooseAction.class, Film.class);
		} else {
			try {

				Film modifiedFilm = ConsoleUtil.modifyObject(film, Film.class);
				if (!DBHelper.updateFilm(modifiedFilm)) {
					System.out
							.println("La película no se ha modificado correctamente.");
					controller.setMenu(ChooseAction.class, Film.class);
					return;
				} else {
					System.out.println("Película modificada correctamente.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			controller.setMenu(MainMenu.class);
		}
	}

}
