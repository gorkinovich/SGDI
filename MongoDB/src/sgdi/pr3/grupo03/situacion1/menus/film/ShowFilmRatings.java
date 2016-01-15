//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion1.menus.film;

import java.util.List;

import sgdi.pr3.grupo03.shared.ConsoleUtil;
import sgdi.pr3.grupo03.situacion1.DBHelper;
import sgdi.pr3.grupo03.situacion1.menus.Menu;
import sgdi.pr3.grupo03.situacion1.menus.control.ViewMenu;
import sgdi.pr3.grupo03.situacion1.model.Film;
import sgdi.pr3.grupo03.situacion1.model.Valuation;

public class ShowFilmRatings extends Menu<String> {

	@Override
	public String draw() {
		System.out
				.println("Inserta el título de la película cuyas valoraciones quieres ver.");

		return ConsoleUtil.getString();
	}

	@Override
	public void input(String filmTitle) {
		Film film = DBHelper.getFilmByTitle(filmTitle);
		if (film == null) {
			System.out
					.println("No se ha encontrado ninguna película con ese título.");
		} else {
			try {
				List<Valuation> valuations = DBHelper
						.getValuationsByRefId(film._id);
				System.out.println("Valoraciones " + valuations);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		controller.setMenu(ViewMenu.class);
	}

}
