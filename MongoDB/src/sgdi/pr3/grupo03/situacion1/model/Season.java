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
import sgdi.pr3.grupo03.shared.ReferenceFieldType;
import sgdi.pr3.grupo03.shared.UserInputField;

//------------------------------------------------------------------------------------------
// Temporada de una serie: sinopsis (explicación de la temporada concreta), año de estreno.
//------------------------------------------------------------------------------------------
@MongoClass()
@UserInputField(outputText = "Temporada")
public class Season {
    @MongoField()
    @UserInputField(outputText = "Referencia a una serie", referenceType = ReferenceFieldType.Series)
    public ObjectId seriesIdRef;
    
    @MongoField(ignoreWhenWrite = true, isMongoId = true)
    @IgnoreInputField()
    public ObjectId _id;

    @MongoField()
    @UserInputField(outputText = "Sinopsis")
    public String synopsis;

    @MongoField()
    @UserInputField(outputText = "Año de estreno")
    public int releaseYear;

    @MongoField(ignoreWhenWrite = true, ignoreWhenRead = true)
    @IgnoreInputField()
    public List<Episode> episodes;

    @MongoField(ignoreWhenWrite = true, ignoreWhenRead = true)
    @IgnoreInputField()
    public List<Valuation> valuations;

    public Season() {}
}
