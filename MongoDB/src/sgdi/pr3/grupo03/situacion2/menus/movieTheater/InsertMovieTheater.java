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
import sgdi.pr3.grupo03.shared.CustomReferenceFieldInput;
import sgdi.pr3.grupo03.shared.ReferenceFieldType;
import sgdi.pr3.grupo03.situacion2.DBHelper;
import sgdi.pr3.grupo03.situacion2.menus.Menu;
import sgdi.pr3.grupo03.situacion2.menus.control.ChooseAction;
import sgdi.pr3.grupo03.situacion2.menus.control.MainMenu;
import sgdi.pr3.grupo03.situacion2.model.Film;
import sgdi.pr3.grupo03.situacion2.model.MovieTheater;

public class InsertMovieTheater extends Menu<Void> {

	@Override
	public Void draw() {
		try {
			MovieTheater film = ConsoleUtil.getObject(MovieTheater.class,
					new DefaultMovieTheaterReferenceInput());
			if (!DBHelper.insertMovieTheater(film)) {
				System.out.println("El cine no se ha añadido correctamente.");
				controller.setMenu(ChooseAction.class, MovieTheater.class);
				return null;
			} else {
				System.out.println("El cine se ha añadido correctamente.");
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

	private class DefaultMovieTheaterReferenceInput implements
			CustomReferenceFieldInput {

		@Override
		public Object getReference(ReferenceFieldType refFieldType,
				String offset) {
			if (refFieldType == ReferenceFieldType.Film) {
				Film film;
				do {
					System.out
							.println("Introduce el título de la película que se proyecta en esta sala");
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

	}
}
