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
// Episodio de una temporada de una serie: titulo, sinopsis, fecha de emisión, actores
// junto con el personaje que interpretan, guionistas, directores.
//------------------------------------------------------------------------------------------
@MongoClass()
@UserInputField(outputText = "Episodio")
public class Episode {
    @MongoField(ignoreWhenWrite = true, isMongoId = true)
    @IgnoreInputField()
    public ObjectId _id;

    @MongoField()
    @UserInputField(outputText = "Referencia a una temporada", referenceType = ReferenceFieldType.Season)
    public ObjectId seasonIdRef;
    
    @MongoField()
    @UserInputField(outputText = "Título")
    public String title;

    @MongoField()
    @UserInputField(outputText = "Sinopsis")
    public String synopsis;

    @MongoField()
    @UserInputField(outputText = "Fecha de emisión")
    public Date releaseDate;

    @MongoField()
    @UserInputField(outputText = "Actores con el personaje que interpretan")
    public List<ActorWithCharacter> actors;

    @MongoField()
    @UserInputField(outputText = "Guionistas")
    public List<String> writers;

    @MongoField()
    @UserInputField(outputText = "Directores")
    public List<String> directors;

    @MongoField(ignoreWhenWrite = true, ignoreWhenRead = true)
    @IgnoreInputField()
    public List<Valuation> valuations;

    public Episode() {}
}
