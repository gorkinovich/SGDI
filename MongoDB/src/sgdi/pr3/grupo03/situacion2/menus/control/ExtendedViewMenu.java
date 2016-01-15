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
import sgdi.pr3.grupo03.situacion2.model.MovieTheater;
import sgdi.pr3.grupo03.situacion2.model.User;
import sgdi.pr3.grupo03.situacion2.model.Valuation;

public class ExtendedViewMenu extends Menu<Integer> {

	private static final int RETURN_INT = -1;

	@Override
	public Integer draw() {
		ConsoleUtil
				.drawMenu(
						"Menu consultar",
						RETURN_INT,
						"Conocer qué usuarios han visto una película o episodio",
						"Consultar las películas y episodios vistos por un usuario",
						"Ver las valoraciones realizadas por un usuario.",
						"Ver aquellas valoraciones muy negativas (puntuación inferior a 4) de un usuario",
						"Ver aquellas valoraciones muy negativas (puntuación inferior a 4) de una película",
						"Consultar la cartelera de un cine dado su nombre.",
						"Consultar las películas (solo su título) que han sido vistas por algún usuario en un cine concreto.",
						"Obtener los cines en los que un usuario (dado su nombre) ha visto películas.",
						"Obtener los usuarios que han visto alguna película en un cine concreto.");

		return ConsoleUtil.getInt(RETURN_INT);
	}

	@Override
	public void input(Integer option) {
		switch (option) {
		case 0:
			// Conocer qué usuarios han visto una película o episodio
			System.out.println("Introduce el título de una película o serie");
			String title = ConsoleUtil.getString();
			List<User> actors = DBHelper.getUsersByTitle(title);
			System.out.println("Usuarios encontrados: " + actors);
			break;
		case 1:
			// Consultar las películas y episodios vistos por un usuario
			System.out.println("Introduce el nombre del usuario");
			String userName = ConsoleUtil.getString();
			List<String> works = DBHelper.getTitlesByUser(userName);
			System.out.println("Películas y series encontrados: " + works);
			break;
		case 2:
			// Ver las valoraciones realizadas por un usuario.
			System.out.println("Introduce el nombre del usuario");
			userName = ConsoleUtil.getString();
			List<Valuation> valuations = DBHelper.getValuationsByUser(userName);
			System.out.println("Valoraciones encontradas: " + valuations);
			break;
		case 3:
			// Ver aquellas valoraciones muy negativas (puntuación inferior a 4)
			// de un usuario
			System.out.println("Introduce el nombre del usuario");
			userName = ConsoleUtil.getString();
			valuations = DBHelper.getLowValuationsByUser(userName);
			System.out.println("Valoraciones inferiores a 4 encontradas: "
					+ valuations);
			break;
		case 4:
			// Ver aquellas valoraciones muy negativas (puntuación inferior a 4)
			// de una película
			System.out.println("Introduce el título de la película");
			title = ConsoleUtil.getString();
			valuations = DBHelper.getLowValuationsByFilm(title);
			System.out.println("Valoraciones de " + title
					+ " inferiores a 4 encontradas: " + valuations);
			break;
		case 5:
			// Consultar la cartelera de un cine dado su nombre.
			System.out.println("Introduce el nombre del cine");
			String movieTheaterName = ConsoleUtil.getString();
			List<String> movieTheaterFilms = DBHelper
					.getMovieTheaterFilmsByName(movieTheaterName);
			System.out.println("Cartelera: " + movieTheaterFilms);
			break;
		case 6:
			// Consultar las películas (solo su título) que han sido vistas por
			// algún usuario en un cine concreto.
			System.out.println("Introduce el nombre del cine");
			movieTheaterName = ConsoleUtil.getString();
			List<String> watchedFilms = DBHelper
					.getWatchedFilmsByMovieTheater(movieTheaterName);
			System.out.println("Películas vistas: " + watchedFilms);
			break;
		case 7:
			// Obtener los cines en los que un usuario (dado su nombre) ha visto
			// películas.
			System.out.println("Introduce el nombre del usuario");
			userName = ConsoleUtil.getString();
			List<MovieTheater> watchedMovieTheatersByUser = DBHelper
					.getWatchedMovieTheatersByUser(userName);
			System.out
					.println("Cines en los que el usuario ha visto películas: "
							+ watchedMovieTheatersByUser);
			break;
		case 8:
			// Obtener los usuarios que han visto alguna película en un cine
			// concreto.
			System.out.println("Introduce el nombre del cine");
			movieTheaterName = ConsoleUtil.getString();
			List<User> usersByMovieTheater = DBHelper
					.getUsersByMovieTheater(movieTheaterName);
			System.out
					.println("usuarios que han visto alguna película en el cine "
							+ movieTheaterName + ": " + usersByMovieTheater);
			break;
		case RETURN_INT:
			controller.setMenu(MainMenu.class);
			break;

		}
	}
}
