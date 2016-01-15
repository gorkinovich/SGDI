//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion2.menus.user;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import sgdi.pr3.grupo03.shared.ConsoleUtil;
import sgdi.pr3.grupo03.shared.CustomReferenceFieldInput;
import sgdi.pr3.grupo03.shared.ReferenceFieldType;
import sgdi.pr3.grupo03.situacion2.DBHelper;
import sgdi.pr3.grupo03.situacion2.menus.Menu;
import sgdi.pr3.grupo03.situacion2.menus.control.ChooseAction;
import sgdi.pr3.grupo03.situacion2.menus.control.MainMenu;
import sgdi.pr3.grupo03.situacion2.model.Episode;
import sgdi.pr3.grupo03.situacion2.model.Film;
import sgdi.pr3.grupo03.situacion2.model.MovieTheater;
import sgdi.pr3.grupo03.situacion2.model.User;

public class InsertUser extends Menu<Void> {

	@Override
	public Void draw() {
		try {
			User film = ConsoleUtil.getObject(User.class,
					new DefaultUserReferenceInput());
			if (!DBHelper.insertUser(film)) {
				System.out.println("Usuario no se ha añadido correctamente.");
				controller.setMenu(ChooseAction.class, User.class);
				return null;
			} else {
				System.out.println("El usuario se ha añadido correctamente.");
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

	public static class DefaultUserReferenceInput implements
			CustomReferenceFieldInput {

		@Override
		public Object getReference(ReferenceFieldType refFieldType,
				String offset) {
			if (refFieldType == ReferenceFieldType.Episode) {
				return getEpisodesList(offset);
			} else if (refFieldType == ReferenceFieldType.MovieTheater) {
				MovieTheater movieTheater;
				do {
					System.out
							.println("Introduce el nombre del cine en el que ha visto la película");
					String name = ConsoleUtil.getString();
					movieTheater = DBHelper.getMovieTheaterByName(name);
					if (movieTheater == null) {
						System.out
								.println("No se ha encontrado ningún cine con ese nombre.");
					}
				} while (movieTheater == null);
				return movieTheater._id;
			} else if (refFieldType == ReferenceFieldType.Film) {
				Film film;
				do {
					System.out
							.println("Introduce el título de la película que ha visto");
					String title = ConsoleUtil.getString();
					film = DBHelper.getFilmByTitle(title);
					if (film == null) {
						System.out
								.println("No se ha encontrado ninga película con ese título.");
					}
				} while (film == null);
				return film._id;
			}
			return null;
		}

		private List<ObjectId> getEpisodesList(String offset) {
			List<ObjectId> episodesIds = new ArrayList<ObjectId>();

			String episodeTitle;
			boolean exit;
			int idx = 0;
			System.out.println(offset
					+ "¿Añadir episodios vistos a la lista? (. para terminar)");
			do {
				System.out.println(offset
						+ "Título del episodio visto (. para terminar)");
				System.out.print(offset + ++idx + " ? ");
				episodeTitle = ConsoleUtil.getString();
				exit = episodeTitle.equals(".");
				if (!exit) {
					Episode episode = DBHelper.getEpisodeByTitle(episodeTitle);
					if (episode == null) {
						System.out
								.println("No se ha encontrado ningún episodio con título "
										+ episodeTitle);
					} else {
						episodesIds.add(episode._id);
					}
				}
			} while (!exit);
			return episodesIds;
		}
	}

}
