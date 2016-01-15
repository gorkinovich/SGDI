//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion2.menus.series;

import java.util.List;

import sgdi.pr3.grupo03.shared.ConsoleUtil;
import sgdi.pr3.grupo03.situacion2.DBHelper;
import sgdi.pr3.grupo03.situacion2.menus.Menu;
import sgdi.pr3.grupo03.situacion2.menus.control.ViewMenu;
import sgdi.pr3.grupo03.situacion2.model.Series;
import sgdi.pr3.grupo03.situacion2.model.Valuation;

public class ShowSeriesRatings extends Menu<String> {

	@Override
	public String draw() {
		System.out
				.println("Inserta el título de la serie cuyas valoraciones quieres ver.");

		return ConsoleUtil.getString();
	}

	@Override
	public void input(String title) {
		Series series = DBHelper.getOneSeriesByTitle(title);
		if (series == null) {
			System.out
					.println("No se ha encontrado ninguna serie con ese título.");
		} else {
			try {
				List<Valuation> valuations = DBHelper
						.getValuationsByRefId(series._id);
				System.out.println("Valoraciones " + valuations);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		controller.setMenu(ViewMenu.class);
	}

}
