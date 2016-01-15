//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion1;

import sgdi.pr3.grupo03.shared.MongoUtil;
import sgdi.pr3.grupo03.situacion1.menus.MenuController;
import sgdi.pr3.grupo03.situacion1.menus.control.MainMenu;

public class Console1 {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            public void run() {
                MongoUtil.close();
            }
        }));
        MenuController menuController = new MenuController();
        menuController.setMenu(MainMenu.class);
        menuController.run();
    }
}
