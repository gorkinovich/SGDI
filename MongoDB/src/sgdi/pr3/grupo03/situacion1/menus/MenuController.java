//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion1.menus;

import java.util.HashMap;
import java.util.Map;

public class MenuController {

    private final Map<Class, Menu> menu = new HashMap<Class, Menu>();
    private Menu currentMenu;

    public void run() {
        while (true) {
            currentMenu.input(currentMenu.draw());
        }
    }

    public void setMenu(Class<? extends Menu> clazz, Object... args) {
        Menu newMenu = menu.get(clazz);
        if (newMenu == null) {
            try {
                newMenu = clazz.newInstance();
                newMenu.setController(this);
                menu.put(clazz, newMenu);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        newMenu.setArgs(args);
        currentMenu = newMenu;
    }
}
