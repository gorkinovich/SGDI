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
import sgdi.pr3.grupo03.shared.ReferenceFieldType;
import sgdi.pr3.grupo03.shared.UserInputField;

//------------------------------------------------------------------------------------------
// Usuarios: nombre, fecha de nacimiento, e-mail, películas que ha visto (y en qué cine y
// qué sala), episodios que ha visto.
//------------------------------------------------------------------------------------------
@MongoClass()
@UserInputField(outputText = "Usuario")
public class User {
    @MongoField(ignoreWhenWrite = true, isMongoId = true)
    @IgnoreInputField()
    public ObjectId _id;

    @MongoField()
    @UserInputField(outputText = "Series vistas", referenceType = ReferenceFieldType.Episode)
    public List<ObjectId> episodeIdRefs;

    @MongoField()
    @UserInputField(outputText = "Películas vistas")
    public List<WatchedFilm> films;

    @MongoField()
    @UserInputField(outputText = "Nombre")
    public String name;

    @MongoField()
    @UserInputField(outputText = "Fecha de nacimiento")
    public Date birthDate;

    @MongoField()
    @UserInputField(outputText = "E-mail")
    public String email;

    public User() {
    }

    @Override
    public String toString() {
        return "{ Nombre: " + name + ", Fecha de nacimiento: "
                + birthDate.toString() + ", Correo: " + email + " }";
    }
}
