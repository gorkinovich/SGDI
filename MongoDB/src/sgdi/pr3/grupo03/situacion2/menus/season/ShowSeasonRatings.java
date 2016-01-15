//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion2.menus.season;

import java.util.List;

import sgdi.pr3.grupo03.shared.ConsoleUtil;
import sgdi.pr3.grupo03.situacion2.DBHelper;
import sgdi.pr3.grupo03.situacion2.menus.Menu;
import sgdi.pr3.grupo03.situacion2.menus.control.MainMenu;
import sgdi.pr3.grupo03.situacion2.menus.control.ViewMenu;
import sgdi.pr3.grupo03.situacion2.model.Season;
import sgdi.pr3.grupo03.situacion2.model.Series;
import sgdi.pr3.grupo03.situacion2.model.Valuation;

public class ShowSeasonRatings extends Menu<String> {

	@Override
	public String draw() {
		System.out
				.println("Inserta el nombre de la serie en la que está la temporada cuyas valoraciones quieres ver.");

		return ConsoleUtil.getString();
	}

	@Override
	public void input(String title) {
		Series series = DBHelper.getOneSeriesByTitle(title);
		if (series == null) {
			System.out
					.println("No se ha encontrado ninguna serie con ese título.");
			controller.setMenu(ViewMenu.class);
		} else {
			System.out.println("Inserta el el año de la temporada de la serie "
					+ title + " cuyas valoraciones quieres ver.");

			int anho = ConsoleUtil.getInt();

			Season season = DBHelper.getSeasonByYear(series._id, anho);

			if (season == null) {
				System.out
						.println("No se ha encontrado ninguna temporada con ese año de estreno en la serie "
								+ title);
				controller.setMenu(ViewMenu.class);
				return;
			}
			try {
				List<Valuation> valuations = DBHelper
						.getValuationsByRefId(season._id);
				System.out.println("Valoraciones " + valuations);
			} catch (Exception e) {
				e.printStackTrace();
			}
			controller.setMenu(MainMenu.class);
		}
	}

}
