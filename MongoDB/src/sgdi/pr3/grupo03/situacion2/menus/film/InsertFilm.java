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

public class InsertFilm extends Menu<Void> {

	@Override
	public Void draw() {
		try {
			Film film = ConsoleUtil.getObject(Film.class, null);
			if (!DBHelper.insertFilm(film)) {
				System.out
						.println("La pelicula no se ha añadido correctamente.");
				controller.setMenu(ChooseAction.class, Film.class);
				return null;
			} else {
				System.out.println("La pelicula se ha añadido correctamente.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		controller.setMenu(MainMenu.class);
		return null;
	}

	@Override
	public void input(Void option) {
	}

}
