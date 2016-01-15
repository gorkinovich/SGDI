//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion2.menus.episode;

import org.bson.types.ObjectId;

import sgdi.pr3.grupo03.shared.ConsoleUtil;
import sgdi.pr3.grupo03.shared.CustomReferenceFieldInput;
import sgdi.pr3.grupo03.shared.ReferenceFieldType;
import sgdi.pr3.grupo03.situacion2.DBHelper;
import sgdi.pr3.grupo03.situacion2.menus.Menu;
import sgdi.pr3.grupo03.situacion2.menus.control.ChooseAction;
import sgdi.pr3.grupo03.situacion2.menus.control.MainMenu;
import sgdi.pr3.grupo03.situacion2.model.Episode;
import sgdi.pr3.grupo03.situacion2.model.Season;
import sgdi.pr3.grupo03.situacion2.model.Series;

public class InsertEpisode extends Menu<Void> {

	@Override
	public Void draw() {
		try {
			Episode episode = ConsoleUtil.getObject(Episode.class,
					new CustomReferenceFieldInput() {

						@Override
						public ObjectId getReference(
								ReferenceFieldType refFieldType, String offset) {
							System.out
									.println(offset
											+ "Inserta el título de la serie en la que estará el episodio que vas a insertar.");

							String seriesTitle = ConsoleUtil.getString();

							Series series = DBHelper
									.getOneSeriesByTitle(seriesTitle);
							if (series == null) {
								System.out
										.println(offset
												+ "No se ha encontrado ninguna serie con título "
												+ seriesTitle);
								return null;
							}

							System.out
									.println(offset
											+ "Inserta el año de estreno de la temporada en la que estará el episodio.");
							int year = ConsoleUtil.getInt();
							Season season = DBHelper.getSeasonByYear(
									series._id, year);
							if (season == null) {
								System.out
										.println(offset
												+ "No se ha encontrado ninguna temporada con año de estreno "
												+ year + " de la serie"
												+ seriesTitle);
								return null;

							}
							return season._id;
						}
					});
			if (episode.seasonIdRef == null || !DBHelper.insertEpisode(episode)) {
				System.out
						.println("El episodio no se ha añadido correctamente.");
				controller.setMenu(ChooseAction.class, Episode.class);
				return null;
			} else {
				System.out.println("El episodio se ha añadido correctamente.");
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
