//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion1.menus.episode;

import sgdi.pr3.grupo03.shared.ConsoleUtil;
import sgdi.pr3.grupo03.situacion1.DBHelper;
import sgdi.pr3.grupo03.situacion1.menus.Menu;
import sgdi.pr3.grupo03.situacion1.menus.control.ChooseAction;
import sgdi.pr3.grupo03.situacion1.menus.control.MainMenu;
import sgdi.pr3.grupo03.situacion1.model.Episode;

public class RemoveEpisode extends Menu<String> {

	@Override
	public String draw() {
		System.out.println("Inserta el nombre del episodio que vas a borrar.");

		return ConsoleUtil.getString();
	}

	@Override
	public void input(String title) {
		Episode series = DBHelper.getEpisodeByTitle(title);
		if (series == null) {
			System.out
					.println("No se ha encontrado ningun episodio con ese título.");
			controller.setMenu(ChooseAction.class, Episode.class);
		} else {
			try {
				if (!DBHelper.removeEpisode(series)) {
					System.out
							.println("El episodio no se ha borrado correctamente.");
					controller.setMenu(ChooseAction.class, Episode.class);
					return;
				} else {
					System.out.println("Episodio borrado correctamente.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			controller.setMenu(MainMenu.class);
		}
	}

}
