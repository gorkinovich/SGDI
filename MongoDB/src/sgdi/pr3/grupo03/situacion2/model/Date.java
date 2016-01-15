//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion2.model;

import sgdi.pr3.grupo03.shared.MongoClass;
import sgdi.pr3.grupo03.shared.MongoField;
import sgdi.pr3.grupo03.shared.UserInputField;

@MongoClass()
@UserInputField(outputText = "Fecha")
public class Date {
    @MongoField()
    @UserInputField(outputText = "Día")
    public int day;

    @MongoField()
    @UserInputField(outputText = "Mes")
    public int month;

    @MongoField()
    @UserInputField(outputText = "Año")
    public int year;

    public Date() {
    }

    @Override
    public String toString() {
        return day + "/" + month + "/" + year;
    }
}
