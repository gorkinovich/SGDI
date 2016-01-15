//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.shared;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;

public class MongoSession {
    // --------------------------------------------------------------------------------------
    // Fields:
    // --------------------------------------------------------------------------------------

    public MongoClient client;
    public DB dataBase;

    public MongoSession() {}

    // --------------------------------------------------------------------------------------
    // General:
    // --------------------------------------------------------------------------------------

    public boolean connect(String dbname) {
        try {
            if (client == null) {
                client = new MongoClient();
            }
            if (dataBase == null || dataBase.getName() == null ||
                !dataBase.getName().equals(dbname)) {
                dataBase = client.getDB(dbname);
            }
            return true;
        } catch (UnknownHostException ex) {
            client = null;
            dataBase = null;
            System.err.println(ex);
            return false;
        }
    }

    public DBCollection getCollection(String cname) {
        return dataBase.getCollection(cname);
    }

    public void close() {
        if (client != null) {
            client.close();
        }
        client = null;
        dataBase = null;
    }

}
