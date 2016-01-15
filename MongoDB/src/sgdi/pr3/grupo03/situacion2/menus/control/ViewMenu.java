//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion2.menus.control;

import java.util.List;

import sgdi.pr3.grupo03.shared.ConsoleUtil;
import sgdi.pr3.grupo03.situacion2.DBHelper;
import sgdi.pr3.grupo03.situacion2.menus.Menu;
import sgdi.pr3.grupo03.situacion2.menus.episode.ShowEpisodeRatings;
import sgdi.pr3.grupo03.situacion2.menus.film.ShowFilmRatings;
import sgdi.pr3.grupo03.situacion2.menus.season.ShowSeasonRatings;
import sgdi.pr3.grupo03.situacion2.menus.series.ShowSeriesRatings;
import sgdi.pr3.grupo03.situacion2.model.Episode;
import sgdi.pr3.grupo03.situacion2.model.Film;
import sgdi.pr3.grupo03.situacion2.model.Season;
import sgdi.pr3.grupo03.situacion2.model.Series;

public class ViewMenu extends Menu<Integer> {

	private static final int RETURN_INT = -1;
	private static final Class[] TYPES = { Film.class, Series.class,
			Season.class, Episode.class };

	@Override
	public Integer draw() {
		ConsoleUtil
				.drawMenu(
						"Menu consultar",
						RETURN_INT,
						"Conocer el nombre de los actores que aparecen en una película o serie de TV (se proporciona el título).",
						"Conocer el título de las películas o series de TV en las que aparece un determinado actor (se proporciona el nombre).",
						"Dado el nombre de un actor y el título de una película o episodio de serie de TV, encontrar el personaje que interpreta.",
						"Consultar las películas grabadas en un determinado país.",
						"Conocer todas las valoraciones para una película, serie, temporada o episodio.");

		return ConsoleUtil.getInt(RETURN_INT);
	}

	@Override
	public void input(Integer option) {
		switch (option) {
		case 0:
			System.out.println("Introduce el título de una película o serie");
			String title = ConsoleUtil.getString();
			List<String> actors = DBHelper.getActorsByTitle(title);
			System.out.println("Actores encontrados: " + actors);
			break;
		case 1:
			System.out.println("Introduce el nombre del actor");
			String actor = ConsoleUtil.getString();
			List<String> works = DBHelper.getWorksByActor(actor);
			System.out.println("Trabajos encontrados: " + works);
			break;
		case 2:
			System.out.println("Introduce el nombre del actor");
			actor = ConsoleUtil.getString();
			System.out.println("Introduce el título de una película o serie");
			title = ConsoleUtil.getString();
			String character = DBHelper.getCharacterByActorAndTitle(actor,
					title);
			if (character == null) {
				System.out
						.println("No se ha encontrado ningún personaje interpretado por "
								+ actor + " en " + title);
			} else {
				System.out.println("El personaje que interpreta " + actor
						+ " en " + title + " es " + character);
			}
			break;
		case 3:
			System.out.println("Introduce el nombre del país");
			String country = ConsoleUtil.getString();
			List<Film> filmsByCountry = DBHelper.getFilmsByCountry(country);
			System.out.println("Las películas grabadas en " + country + " son "
					+ filmsByCountry);
			break;
		case 4:
			ConsoleUtil.drawMenu("Elige un tipo", RETURN_INT, TYPES);
			int res = ConsoleUtil.getInt(RETURN_INT);
			if (res >= 0 && res < TYPES.length) {
				Class clazz = TYPES[res];
				if (clazz == Film.class) {
					controller.setMenu(ShowFilmRatings.class);
				} else if (clazz == Series.class) {
					controller.setMenu(ShowSeriesRatings.class);
				} else if (clazz == Season.class) {
					controller.setMenu(ShowSeasonRatings.class);
				} else if (clazz == Episode.class) {
					controller.setMenu(ShowEpisodeRatings.class);
				}
			} else if (res == RETURN_INT) {
				controller.setMenu(MainMenu.class);
			}
			break;
		case RETURN_INT:
			controller.setMenu(MainMenu.class);
			break;

		}
	}

	private Class askForType(Class... types) {
		ConsoleUtil.drawMenu("Elige un tipo: ", RETURN_INT, types);
		int res = ConsoleUtil.getInt(RETURN_INT);
		if (res >= 0 && res < types.length) {
			return types[res];
		}
		return null;
	}

	private String askFortitle(Class clazz) {
		System.out.println("Introduce el título de la "
				+ ConsoleUtil.getOutputText(clazz));
		return ConsoleUtil.getString();
	}

}
