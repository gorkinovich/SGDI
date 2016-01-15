//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion1.model;

import sgdi.pr3.grupo03.shared.MongoClass;
import sgdi.pr3.grupo03.shared.MongoField;
import sgdi.pr3.grupo03.shared.UserInputField;

@MongoClass()
@UserInputField(outputText = "Actor con personaje que interpreta")
public class ActorWithCharacter {
    @MongoField()
    @UserInputField(outputText = "Actor")
    public String actor;

    @MongoField()
    @UserInputField(outputText = "Personaje que interpreta")
    public String character;

    public ActorWithCharacter() {}
}
