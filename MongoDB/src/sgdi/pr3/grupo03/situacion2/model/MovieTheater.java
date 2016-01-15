//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion2.model;

import java.util.List;

import org.bson.types.ObjectId;

import sgdi.pr3.grupo03.shared.IgnoreInputField;
import sgdi.pr3.grupo03.shared.MongoClass;
import sgdi.pr3.grupo03.shared.MongoField;
import sgdi.pr3.grupo03.shared.UserInputField;

//------------------------------------------------------------------------------------------
// Cines: nombre, dirección, salas y su aforo, cartelera actual (qué película
// se proyecta en cada sala).
//------------------------------------------------------------------------------------------
@MongoClass()
@UserInputField(outputText = "Cine")
public class MovieTheater {
    @MongoField(ignoreWhenWrite = true, isMongoId = true)
    @IgnoreInputField()
    public ObjectId _id;

    @MongoField()
    @UserInputField(outputText = "Nombre")
    public String name;

    @MongoField()
    @UserInputField(outputText = "Dirección")
    public String address;

    @MongoField()
    @UserInputField(outputText = "Salas")
    public List<Auditorium> auditoriums;

    public MovieTheater() {}
    
    @Override
    public String toString() {
        return name;
    }
}
