//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion1.menus.season;

import org.bson.types.ObjectId;

import sgdi.pr3.grupo03.shared.ConsoleUtil;
import sgdi.pr3.grupo03.shared.CustomReferenceFieldInput;
import sgdi.pr3.grupo03.shared.ReferenceFieldType;
import sgdi.pr3.grupo03.situacion1.DBHelper;
import sgdi.pr3.grupo03.situacion1.menus.Menu;
import sgdi.pr3.grupo03.situacion1.menus.control.ChooseAction;
import sgdi.pr3.grupo03.situacion1.menus.control.MainMenu;
import sgdi.pr3.grupo03.situacion1.model.Season;
import sgdi.pr3.grupo03.situacion1.model.Series;

public class InsertSeason extends Menu<Void> {

	@Override
	public Void draw() {
		try {
			Season season = ConsoleUtil.getObject(Season.class,
					new CustomReferenceFieldInput() {

						@Override
						public ObjectId getReference(
								ReferenceFieldType refFieldType, String offset) {
							System.out
									.println(offset
											+ "Inserta el nombre de la serie en la que estará la temporada.");
							String seriesTitle = ConsoleUtil.getString();
							Series serie = DBHelper
									.getOneSeriesByTitle(seriesTitle);
							if (serie == null) {
								System.out
										.println(offset
												+ "No se ha encontrado ninguna serie con el título: "
												+ seriesTitle);
								return null;

							}
							return serie._id;
						}
					});
			if (season.seriesIdRef == null || !DBHelper.insertSeason(season)) {
				System.out
						.println("La temporada no se ha añadido correctamente.");
				controller.setMenu(ChooseAction.class, Season.class);
				return null;
			} else {
				System.out.println("La temporada se ha añadido correctamente.");
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
