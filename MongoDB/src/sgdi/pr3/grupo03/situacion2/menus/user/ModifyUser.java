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
import sgdi.pr3.grupo03.situacion2.menus.user.InsertUser.DefaultUserReferenceInput;
import sgdi.pr3.grupo03.situacion2.model.User;

public class ModifyUser extends Menu<String> {

	@Override
	public String draw() {
		System.out
				.println("Inserta el nombre del usuario que vas a modificar.");

		return ConsoleUtil.getString();
	}

	@Override
	public void input(String userName) {
		User user = DBHelper.getUserByName(userName);
		if (user == null) {
			System.out
					.println("No se ha encontrado ningun usuario con ese nombre.");
			controller.setMenu(ChooseAction.class, User.class);
		} else {
			try {

				User modifiedFilm = ConsoleUtil.modifyObject(user,
						new DefaultUserReferenceInput(), User.class);
				if (!DBHelper.updateUser(modifiedFilm)) {
					System.out
							.println("El usuario no se ha modificado correctamente.");
					controller.setMenu(ChooseAction.class, User.class);
					return;
				} else {
					System.out.println("Usuario modificado correctamente.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			controller.setMenu(MainMenu.class);
		}
	}

}
