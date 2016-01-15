//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion2.model;

import org.bson.types.ObjectId;

import sgdi.pr3.grupo03.shared.MongoClass;
import sgdi.pr3.grupo03.shared.MongoField;
import sgdi.pr3.grupo03.shared.ReferenceFieldType;
import sgdi.pr3.grupo03.shared.UserInputField;

@MongoClass()
@UserInputField(outputText = "Sala")
public class Auditorium {
    @MongoField()
    @UserInputField(outputText = "Película", referenceType = ReferenceFieldType.Film)
    public ObjectId filmIdRef;

    @MongoField()
    @UserInputField(outputText = "Número de la sala")
    public int number;

    @MongoField()
    @UserInputField(outputText = "Aforo")
    public int capacity;

    public Auditorium() {
    }
}
