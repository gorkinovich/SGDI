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
import sgdi.pr3.grupo03.situacion1.model.Season;

public class ModifyEpisode extends Menu<String> {

	@Override
	public String draw() {
		System.out
				.println("Inserta el título del episodio que vas a modificar.");

		return ConsoleUtil.getString();
	}

	@Override
	public void input(String title) {
		Episode episode = DBHelper.getEpisodeByTitle(title);
		if (episode == null) {
			System.out
					.println("No se ha encontrado ningún episodio con ese título.");
			controller.setMenu(ChooseAction.class, Episode.class);
		} else {

			try {

				Episode modifiedEpisode = ConsoleUtil.modifyObject(episode,
						Episode.class);
				if (!DBHelper.updateEpisode(modifiedEpisode)) {
					System.out
							.println("El episodio no se ha modificado correctamente.");
					controller.setMenu(ChooseAction.class, Season.class);
					return;
				} else {
					System.out.println("Episdio modificado correctamente.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			controller.setMenu(MainMenu.class);
		}
	}

}
