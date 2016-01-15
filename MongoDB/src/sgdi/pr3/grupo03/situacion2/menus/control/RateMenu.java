//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion2.menus.control;

import org.bson.types.ObjectId;

import sgdi.pr3.grupo03.shared.ConsoleUtil;
import sgdi.pr3.grupo03.shared.CustomReferenceFieldInput;
import sgdi.pr3.grupo03.shared.ReferenceFieldType;
import sgdi.pr3.grupo03.situacion2.DBHelper;
import sgdi.pr3.grupo03.situacion2.menus.Menu;
import sgdi.pr3.grupo03.situacion2.model.Episode;
import sgdi.pr3.grupo03.situacion2.model.Film;
import sgdi.pr3.grupo03.situacion2.model.Season;
import sgdi.pr3.grupo03.situacion2.model.Series;
import sgdi.pr3.grupo03.situacion2.model.User;
import sgdi.pr3.grupo03.situacion2.model.Valuation;

public class RateMenu extends Menu<Integer> {

	private static final int RETURN_INT = -1;
	private static final Class[] TYPES = { Film.class, Series.class,
			Season.class, Episode.class };

	@Override
	public Integer draw() {
		try {
			Valuation valuation = ConsoleUtil.getObject(Valuation.class,
					new DefaultValuationReferenceInput());

			if (valuation.idRef == null || valuation.userIdRef == null
					|| !DBHelper.insertValuation(valuation)) {
				System.out
						.println("La valoración no se ha añadido correctamente.");
			} else {
				System.out
						.println("La valoración se ha añadido correctamente.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void input(Integer res) {
		controller.setMenu(MainMenu.class);
	}

	private class DefaultValuationReferenceInput implements
			CustomReferenceFieldInput {
		@Override
		public ObjectId getReference(ReferenceFieldType refFieldType,
				String offset) {
			if (refFieldType == ReferenceFieldType.Generic) {
				ConsoleUtil.drawMenu(offset + "Elige lo que quieres valorar",
						RETURN_INT, TYPES);
				int res = ConsoleUtil.getInt(RETURN_INT);
				if (res >= 0 && res < TYPES.length) {
					Class clazz = TYPES[res];
					if (clazz == Film.class) {
						System.out
								.println(offset
										+ "Introduce el título de la película que vas a valorar");
						String title = ConsoleUtil.getString();
						Film film = DBHelper.getFilmByTitle(title);
						if (film == null) {
							System.out
									.println(offset
											+ "No se ha encontrado ninguna película con el título "
											+ title);
							return null;
						}
						return film._id;
					} else if (clazz == Series.class) {
						System.out
								.println(offset
										+ "Introduce el título de la serie que vas a valorar");
						String title = ConsoleUtil.getString();
						Series series = DBHelper.getOneSeriesByTitle(title);
						if (series == null) {
							System.out
									.println(offset
											+ "No se ha encontrado ninguna serie con el título "
											+ title);
							return null;
						}
						return series._id;
					} else if (clazz == Season.class) {
						System.out
								.println(offset
										+ "Inserta el nombre de la serie en la que está la temporada que vas a modificar.");

						String title = ConsoleUtil.getString();

						Series series = DBHelper.getOneSeriesByTitle(title);
						if (series == null) {
							System.out
									.println(offset
											+ "No se ha encontrado ninguna serie con ese título.");
							return null;
						} else {
							System.out
									.println(offset
											+ "Inserta el el año de la temporada de la serie "
											+ title + " que vas a modificar.");

							int year = ConsoleUtil.getInt();

							Season season = DBHelper.getSeasonByYear(
									series._id, year);

							if (season == null) {
								System.out
										.println(offset
												+ "No se ha encontrado ninguna temporada con ese año de estreno en la serie "
												+ title);
								controller.setMenu(ChooseAction.class,
										Season.class);
								return null;
							}
							return season._id;
						}

					} else if (clazz == Episode.class) {
						System.out
								.println(offset
										+ "Introduce el título del episodio que vas a valorar");
						String title = ConsoleUtil.getString();
						Episode episode = DBHelper.getEpisodeByTitle(title);
						if (episode == null) {
							System.out
									.println(offset
											+ "No se ha encontrado ningún episodio con el título "
											+ title);
							return null;
						}
						return episode._id;
					}
				} else if (res == RETURN_INT) {
					controller.setMenu(MainMenu.class);
				}
			} else {
				// Usuario asociado
				System.out.println(offset
						+ "Introduce el nombre del usuario que va a valorar");
				String name = ConsoleUtil.getString();
				User user = DBHelper.getUserByName(name);
				if (user == null) {
					System.out
							.println(offset
									+ "No se ha encontrado ningún usuario con el nombre "
									+ name);
					return null;
				}
				return user._id;
			}
			return null;
		}
	}
}
