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
import sgdi.pr3.grupo03.situacion2.menus.episode.InsertEpisode;
import sgdi.pr3.grupo03.situacion2.menus.episode.ModifyEpisode;
import sgdi.pr3.grupo03.situacion2.menus.episode.RemoveEpisode;
import sgdi.pr3.grupo03.situacion2.menus.film.InsertFilm;
import sgdi.pr3.grupo03.situacion2.menus.film.ModifyFilm;
import sgdi.pr3.grupo03.situacion2.menus.film.RemoveFilm;
import sgdi.pr3.grupo03.situacion2.menus.movieTheater.InsertMovieTheater;
import sgdi.pr3.grupo03.situacion2.menus.movieTheater.ModifyMovieTheater;
import sgdi.pr3.grupo03.situacion2.menus.movieTheater.RemoveMovieTheater;
import sgdi.pr3.grupo03.situacion2.menus.season.InsertSeason;
import sgdi.pr3.grupo03.situacion2.menus.season.ModifySeason;
import sgdi.pr3.grupo03.situacion2.menus.season.RemoveSeason;
import sgdi.pr3.grupo03.situacion2.menus.series.InsertSeries;
import sgdi.pr3.grupo03.situacion2.menus.series.ModifySeries;
import sgdi.pr3.grupo03.situacion2.menus.series.RemoveSeries;
import sgdi.pr3.grupo03.situacion2.menus.user.InsertUser;
import sgdi.pr3.grupo03.situacion2.menus.user.ModifyUser;
import sgdi.pr3.grupo03.situacion2.menus.user.RemoveUser;
import sgdi.pr3.grupo03.situacion2.model.Episode;
import sgdi.pr3.grupo03.situacion2.model.Film;
import sgdi.pr3.grupo03.situacion2.model.MovieTheater;
import sgdi.pr3.grupo03.situacion2.model.Season;
import sgdi.pr3.grupo03.situacion2.model.Series;
import sgdi.pr3.grupo03.situacion2.model.User;

public class ChooseAction extends Menu<Integer> {

	private static final int RETURN_INT = -1;

	@Override
	public Integer draw() {
		ConsoleUtil.drawMenu(
				"Elige una acción para " + ConsoleUtil.getOutputText(args[0]),
				RETURN_INT, "Insertar", "Modificar", "Borrar");

		return ConsoleUtil.getInt(RETURN_INT);
	}

	@Override
	public void input(Integer option) {
		switch (option) {
		case 0:
			insert((Class) args[0]);
			break;
		case 1:
			modify((Class) args[0]);
			break;
		case 2:
			remove((Class) args[0]);
			break;
		case RETURN_INT:
			controller.setMenu(EditMenu.class);
			break;

		}
	}

	private void insert(Class clazz) {
		if (clazz == Film.class) {
			controller.setMenu(InsertFilm.class);
		} else if (clazz == Series.class) {
			controller.setMenu(InsertSeries.class);
		} else if (clazz == Season.class) {
			controller.setMenu(InsertSeason.class);
		} else if (clazz == Episode.class) {
			controller.setMenu(InsertEpisode.class);
		} else if (clazz == User.class) {
			controller.setMenu(InsertUser.class);
		} else if (clazz == MovieTheater.class) {
			controller.setMenu(InsertMovieTheater.class);
		}
	}

	private void modify(Class clazz) {
		if (clazz == Film.class) {
			controller.setMenu(ModifyFilm.class);
		} else if (clazz == Series.class) {
			controller.setMenu(ModifySeries.class);
		} else if (clazz == Season.class) {
			controller.setMenu(ModifySeason.class);
		} else if (clazz == Episode.class) {
			controller.setMenu(ModifyEpisode.class);
		} else if (clazz == User.class) {
			controller.setMenu(ModifyUser.class);
		} else if (clazz == MovieTheater.class) {
			controller.setMenu(ModifyMovieTheater.class);
		}
	}

	private void remove(Class clazz) {
		if (clazz == Film.class) {
			controller.setMenu(RemoveFilm.class);
		} else if (clazz == Series.class) {
			controller.setMenu(RemoveSeries.class);
		} else if (clazz == Season.class) {
			controller.setMenu(RemoveSeason.class);
		} else if (clazz == Episode.class) {
			controller.setMenu(RemoveEpisode.class);
		} else if (clazz == User.class) {
			controller.setMenu(RemoveUser.class);
		} else if (clazz == MovieTheater.class) {
			controller.setMenu(RemoveMovieTheater.class);
		}
	}

}
