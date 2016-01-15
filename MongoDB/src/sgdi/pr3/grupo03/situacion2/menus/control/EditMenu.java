//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion2.menus.control;

import sgdi.pr3.grupo03.shared.ConsoleUtil;
import sgdi.pr3.grupo03.situacion2.menus.Menu;
import sgdi.pr3.grupo03.situacion2.model.Episode;
import sgdi.pr3.grupo03.situacion2.model.Film;
import sgdi.pr3.grupo03.situacion2.model.MovieTheater;
import sgdi.pr3.grupo03.situacion2.model.Season;
import sgdi.pr3.grupo03.situacion2.model.Series;
import sgdi.pr3.grupo03.situacion2.model.User;

public class EditMenu extends Menu<Integer> {

	private static final int RETURN_INT = -1;
	private static final Class[] TYPES = { Film.class, Series.class,
			Season.class, Episode.class, User.class, MovieTheater.class };

	@Override
	public Integer draw() {
		ConsoleUtil.drawMenu("Menu editar", RETURN_INT, TYPES);

		return ConsoleUtil.getInt(RETURN_INT);
	}

	@Override
	public void input(Integer option) {
		switch (option) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			controller.setMenu(ChooseAction.class, TYPES[option]);
			break;
		case RETURN_INT:
			controller.setMenu(MainMenu.class);
			break;

		}
	}

}
