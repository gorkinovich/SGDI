//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.situacion1.model;

import org.bson.types.ObjectId;

import sgdi.pr3.grupo03.shared.IgnoreInputField;
import sgdi.pr3.grupo03.shared.MongoClass;
import sgdi.pr3.grupo03.shared.MongoField;
import sgdi.pr3.grupo03.shared.ReferenceFieldType;
import sgdi.pr3.grupo03.shared.UserInputField;

//------------------------------------------------------------------------------------------
// Valoraciones de las películas, series, temporadas y episodios: titulo de la valoración,
// puntuación (de 0 a 10 sin decimales), mensaje explicando la valoración.
//------------------------------------------------------------------------------------------
@MongoClass()
@UserInputField(outputText = "Valoración")
public class Valuation {
    @MongoField()
    @UserInputField(outputText = "Elemento asociado", referenceType = ReferenceFieldType.Generic)
    public ObjectId idRef;
    
    @MongoField(ignoreWhenWrite = true, isMongoId = true)
    @IgnoreInputField()
    public ObjectId _id;

    @MongoField()
    @UserInputField(outputText = "Título")
    public String title;

    @MongoField()
    @UserInputField(outputText = "Puntuación (del 0 al 10 sin decimales)")
    public int punctuation;

    @MongoField()
    @UserInputField(outputText = "Explicación")
    public String explanation;

    public Valuation() {
    }

    @Override
    public String toString() {
        return "{ Título: " + title + ", Explicación: " + explanation
                + ", Puntuación (del 0 al 10 sin decimales): " + punctuation
                + " }";
    }
}
