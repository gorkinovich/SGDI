//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion1.model;

import java.util.List;

import org.bson.types.ObjectId;

import sgdi.pr3.grupo03.shared.IgnoreInputField;
import sgdi.pr3.grupo03.shared.MongoClass;
import sgdi.pr3.grupo03.shared.MongoField;
import sgdi.pr3.grupo03.shared.UserInputField;

//------------------------------------------------------------------------------------------
// Películas: título, año de estreno, directores, países en los que se ha grabado,
// sinopsis, actores junto con el personaje que interpretan, guionistas.
//------------------------------------------------------------------------------------------
@MongoClass()
@UserInputField(outputText = "Película")
public class Film {
    @MongoField(ignoreWhenWrite = true, isMongoId = true)
    @IgnoreInputField()
    public ObjectId _id;

    @MongoField()
    @UserInputField(outputText = "Título")
    public String title;

    @MongoField()
    @UserInputField(outputText = "Año de estreno")
    public int releaseYear;

    @MongoField()
    @UserInputField(outputText = "Directores")
    public List<String> directors;

    @MongoField()
    @UserInputField(outputText = "Paises en los que se ha grabado")
    public List<String> countriesWhereFilmed;

    @MongoField()
    @UserInputField(outputText = "Actores con el personaje que interpretan")
    public List<ActorWithCharacter> actors;

    @MongoField()
    @UserInputField(outputText = "Sinopsis")
    public String synopsis;

    @MongoField()
    @UserInputField(outputText = "Guionistas")
    public List<String> writers;

    @MongoField(ignoreWhenWrite = true, ignoreWhenRead = true)
    @IgnoreInputField()
    public List<Valuation> valuations;

    public Film() {
    }

    @Override
    public String toString() {
        return title;
    }
}
