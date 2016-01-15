//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion1.menus.season;

import sgdi.pr3.grupo03.shared.ConsoleUtil;
import sgdi.pr3.grupo03.situacion1.DBHelper;
import sgdi.pr3.grupo03.situacion1.menus.Menu;
import sgdi.pr3.grupo03.situacion1.menus.control.ChooseAction;
import sgdi.pr3.grupo03.situacion1.menus.control.MainMenu;
import sgdi.pr3.grupo03.situacion1.model.Season;
import sgdi.pr3.grupo03.situacion1.model.Series;

public class RemoveSeason extends Menu<String> {

	@Override
	public String draw() {
		System.out
				.println("Inserta el nombre de la serie en la que está la temporada que vas a borrar.");

		return ConsoleUtil.getString();
	}

	@Override
	public void input(String title) {
		Series series = DBHelper.getOneSeriesByTitle(title);
		if (series == null) {
			System.out
					.println("No se ha encontrado ninguna serie con ese título.");
			controller.setMenu(ChooseAction.class, Season.class);
		} else {
			System.out.println("Inserta el el año de la temporada de la serie "
					+ title + " que vas a borrar.");

			int anho = ConsoleUtil.getInt();

			Season season = DBHelper.getSeasonByYear(series._id, anho);

			if (season == null) {
				System.out
						.println("No se ha encontrado ninguna temporada con ese año de estreno en la serie " + title);
				controller.setMenu(ChooseAction.class, Season.class);
				return;
			}
			try {

				if (!DBHelper.removeSeason(season)) {
					System.out
							.println("La temporada no se ha borrado correctamente.");
					controller.setMenu(ChooseAction.class, Season.class);
					return;
				} else {
					System.out.println("Temporada borrada correctamente.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			controller.setMenu(MainMenu.class);
		}
	}

}
