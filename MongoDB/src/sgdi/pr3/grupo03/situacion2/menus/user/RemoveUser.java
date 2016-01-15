//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion2.menus.user;

import sgdi.pr3.grupo03.shared.ConsoleUtil;
import sgdi.pr3.grupo03.situacion2.DBHelper;
import sgdi.pr3.grupo03.situacion2.menus.Menu;
import sgdi.pr3.grupo03.situacion2.menus.control.ChooseAction;
import sgdi.pr3.grupo03.situacion2.menus.control.MainMenu;
import sgdi.pr3.grupo03.situacion2.model.User;

public class RemoveUser extends Menu<String> {

	@Override
	public String draw() {
		System.out
				.println("Inserta el nombre del usuario que vas a borrar.");

		return ConsoleUtil.getString();
	}

	@Override
	public void input(String userName) {
		User user = DBHelper.getUserByName(userName);
		if (user == null) {
			System.out
					.println("No se ha encontrado ningún usuario con ese nombre.");
			controller.setMenu(ChooseAction.class, User.class);
		} else {
			try {
				if (!DBHelper.removeUser(user)) {
					System.out
							.println("El usuario no se ha borrado correctamente.");
					controller.setMenu(ChooseAction.class, User.class);
					return;
				} else {
					System.out.println("Usuario borrado correctamente.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			controller.setMenu(MainMenu.class);
		}
	}

}
