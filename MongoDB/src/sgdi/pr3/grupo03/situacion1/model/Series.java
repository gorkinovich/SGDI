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
// Serie de TV: título, año de inicio, sinopsis (explicación general de la serie).
//------------------------------------------------------------------------------------------
@MongoClass()
@UserInputField(outputText = "Serie")
public class Series {
    @MongoField(ignoreWhenWrite = true, isMongoId = true)
    @IgnoreInputField()
    public ObjectId _id;

    @MongoField()
    @UserInputField(outputText = "Título")
    public String title;

    @MongoField()
    @UserInputField(outputText = "Año de inicio")
    public int releaseYear;

    @MongoField()
    @UserInputField(outputText = "Sinopsis")
    public String synopsis;

    @MongoField(ignoreWhenWrite = true, ignoreWhenRead = true)
    @IgnoreInputField()
    public List<Season> seasons;

    @MongoField(ignoreWhenWrite = true, ignoreWhenRead = true)
    @IgnoreInputField()
    public List<Valuation> valuations;

    public Series() {}
}
