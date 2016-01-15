//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion2.menus.series;

import sgdi.pr3.grupo03.shared.ConsoleUtil;
import sgdi.pr3.grupo03.situacion2.DBHelper;
import sgdi.pr3.grupo03.situacion2.menus.Menu;
import sgdi.pr3.grupo03.situacion2.menus.control.ChooseAction;
import sgdi.pr3.grupo03.situacion2.menus.control.MainMenu;
import sgdi.pr3.grupo03.situacion2.model.Series;

public class ModifySeries extends Menu<String> {

	@Override
	public String draw() {
		System.out
				.println("Inserta el nombre de la serie que vas a modificar.");

		return ConsoleUtil.getString();
	}

	@Override
	public void input(String title) {
		Series series = DBHelper.getOneSeriesByTitle(title);
		if (series == null) {
			System.out
					.println("No se ha encontrado ninguna serie con ese título.");
			controller.setMenu(ChooseAction.class, Series.class);
		} else {
			try {

				Series modifiedSeries = ConsoleUtil.modifyObject(series,
						Series.class);
				if (!DBHelper.updateSeries(modifiedSeries)) {
					System.out
							.println("La serie no se ha modificado correctamente.");
					controller.setMenu(ChooseAction.class, Series.class);
					return;
				} else {
					System.out.println("Serie modificada correctamente.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			controller.setMenu(MainMenu.class);
		}
	}

}
