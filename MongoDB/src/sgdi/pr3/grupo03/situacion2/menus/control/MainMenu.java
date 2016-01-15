//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion2.menus.control;

import sgdi.pr3.grupo03.shared.ConsoleUtil;
import sgdi.pr3.grupo03.situacion2.menus.Menu;

public class MainMenu extends Menu<Integer> {

    private static final int RETURN_INT = -1;

    @Override
    public Integer draw() {
        ConsoleUtil.drawMenu("Menu principal", RETURN_INT,
                "Editar(pelicula, serie, temporada, episodio)",
                "Valorar(pelicula, serie, temporada, episodio)", "Consultas", "Más consultas...");

        return ConsoleUtil.getInt(RETURN_INT);
    }

    @Override
    public void input(Integer option) {
        switch (option) {
        case 0:
            controller.setMenu(EditMenu.class);
            break;
        case 1:
            controller.setMenu(RateMenu.class);
            break;
        case 2:
            controller.setMenu(ViewMenu.class);
            break;
        case 3:
            controller.setMenu(ExtendedViewMenu.class);
            break;
        case RETURN_INT:
            System.exit(0);
            break;
        }
    }

}
