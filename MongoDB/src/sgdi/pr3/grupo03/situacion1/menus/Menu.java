//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion1.menus;

public abstract class Menu<T> {
    protected Object[] args;
    protected MenuController controller;

    public Menu() {

    }

    public abstract T draw();

    public abstract void input(T option);

    public void setController(MenuController controller) {
        this.controller = controller;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
