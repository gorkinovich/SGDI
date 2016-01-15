//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.shared;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MongoUtil {
    // --------------------------------------------------------------------------------------
    // Mongo Session:
    // --------------------------------------------------------------------------------------

    private static MongoSession session = null;

    private static boolean connect(String dbname) {
        if (session == null) {
            session = new MongoSession();
        }
        return session.connect(dbname);
    }

    public static void close() {
        if (session != null) {
            session.close();
            session = null;
        }
    }

    // --------------------------------------------------------------------------------------
    // Insert/Update/Remove:
    // --------------------------------------------------------------------------------------

    public static DBObject createQuery(DBObject document, String field) {
        if (document != null && document.containsField(field)) {
            Object value = document.get(field);
            if (value != null) {
                return new BasicDBObject(field, value);
            }
        }
        return null;
    }

    public static boolean insert(String dbname, String cname, DBObject document) {
        boolean success = false;
        if (document != null) {
            if (connect(dbname)) {
                DBCollection collection = session.getCollection(cname);
                if (collection.findOne(document) == null) {
                    collection.insert(document);
                    success = true;
                }
            }
        }
        return success;
    }

    public static boolean update(String dbname, String cname,
            DBObject document, String field) {
        boolean success = false;
        if (document != null) {
            if (connect(dbname)) {
                DBCollection collection = session.getCollection(cname);
                DBObject query = createQuery(document, field);
                if (query != null) {
                    collection.update(query, document);
                }
                success = true;
            }
        }
        return success;
    }

    public static boolean remove(String dbname, String cname,
            DBObject document, String field) {
        boolean success = false;
        if (document != null) {
            if (connect(dbname)) {
                DBCollection collection = session.getCollection(cname);
                DBObject query = createQuery(document, field);
                if (query != null) {
                    collection.remove(query);
                }
                success = true;
            }
        }
        return success;
    }

    public static boolean updateById(String dbname, String cname,
            DBObject document) {
        return update(dbname, cname, document, "_id");
    }

    public static boolean removeById(String dbname, String cname,
            DBObject document) {
        return remove(dbname, cname, document, "_id");
    }

    // --------------------------------------------------------------------------------------
    // Find:
    // --------------------------------------------------------------------------------------

    public static DBCursor find(String dbname, String cname, DBObject query) {
        DBCursor result = null;
        if (query != null) {
            
            if (connect(dbname)) {
                DBCollection collection = session.getCollection(cname);
                result = collection.find(query);
            }
        }
        return result;
    }

    public static DBObject findOne(String dbname, String cname, DBObject query) {
        DBObject result = null;
        if (query != null) {
            
            if (connect(dbname)) {
                DBCollection collection = session.getCollection(cname);
                result = collection.findOne(query);
            }
        }
        return result;
    }

    public static DBCursor find(String dbname, String cname, String field,
            Object value) {
        return find(dbname, cname, new BasicDBObject(field, value));
    }

    public static DBObject findOne(String dbname, String cname, String field,
            Object value) {
        return findOne(dbname, cname, new BasicDBObject(field, value));
    }

    public static DBCursor find(String dbname, CustomFind query) {
        DBCursor result = null;
        if (query != null) {
            
            if (connect(dbname)) {
                result = query.execute(session);
            }
        }
        return result;
    }

    public static DBObject findOne(String dbname, CustomFindOne query) {
        DBObject result = null;
        if (query != null) {
            
            if (connect(dbname)) {
                result = query.execute(session);
            }
        }
        return result;
    }

    // --------------------------------------------------------------------------------------
    // From type to DBObject:
    // --------------------------------------------------------------------------------------

    public static <T extends Object> DBObject toDBObject(T victim, boolean writeMongoId) {
        if (victim != null) {
            Class<?> type = victim.getClass();
            if (isMongoClass(type)) {
                BasicDBObject document = null;
                Field[] fields = type.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    MongoField annotation = getMongoField(field);
                    if (annotation != null && ((writeMongoId && annotation.isMongoId()) ||
                        !annotation.ignoreWhenWrite())) {
                        try {
                            String name = annotation.name();
                            Object value = field.get(victim);
                            if (name.isEmpty()) {
                                name = field.getName();
                            }
                            if (isMongoClass(value.getClass())) {
                                value = toDBObject(value, writeMongoId);
                            } else if (value instanceof List) {
                                value = toDBListObject((List) value, writeMongoId);
                            }
                            if (document == null) {
                                document = new BasicDBObject(name, value);
                            } else {
                                document.append(name, value);
                            }
                        } catch (IllegalArgumentException
                                | IllegalAccessException ex) {
                            System.err.println(ex);
                        }
                    }
                    field.setAccessible(false);
                }
                return document;
            }
        }
        return null;
    }

    public static DBObject toDBListObject(List victims, boolean writeMongoId) {
        BasicDBList list = new BasicDBList();
        if (victims != null) {
            for (Object victim : victims) {
                if (isMongoClass(victim.getClass())) {
                    list.add(toDBObject(victim, writeMongoId));
                } else if (victim instanceof List) {
                    list.add(toDBListObject((List) victim, writeMongoId));
                } else {
                    list.add(victim);
                }
            }
        }
        return list;
    }

    // --------------------------------------------------------------------------------------
    // From DBObject to type:
    // --------------------------------------------------------------------------------------

    public static <T extends Object> T toUserObject(Class<T> type,
            DBObject document) {
        if (document != null && isMongoClass(type)) {
            try {
                Constructor<T> constructor = type.getConstructor();
                T victim = constructor.newInstance();
                Field[] fields = type.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    MongoField annotation = getMongoField(field);
                    if (annotation != null && !annotation.ignoreWhenRead()) {
                        String name = annotation.name();
                        if (name.isEmpty()) {
                            name = field.getName();
                        }
                        if (document.containsField(name)) {
                            Object value = document.get(name);
                            if (value != null) {
                                if (value instanceof List) {
                                    value = toUserObject(
                                            ((ParameterizedType) field
                                                    .getGenericType())
                                                    .getActualTypeArguments()[0],
                                            (List) value);
                                } else if (value instanceof DBObject) {
                                    value = toUserObject(field.getType(),
                                            (DBObject) value);
                                }
                                field.set(victim, value);
                            }
                        }
                    }
                    field.setAccessible(false);
                }
                return victim;

            } catch (NoSuchMethodException | SecurityException
                    | InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException ex) {
                System.err.println(ex);
            }
        }
        return null;
    }

    public static List toUserObject(Type ltype, List list) {
        if (list == null) {
            return null;
        }
        List victims = new ArrayList();
        if (ltype instanceof Class<?>) {
            Class<?> type = (Class<?>) ltype;
            if (isMongoClass(type)) {
                for (Object item : list) {
                    victims.add(toUserObject(type, (DBObject) item));
                }
            } else {
                for (Object item : list) {
                    victims.add(item);
                }
            }
        } else {
            ParameterizedType type = (ParameterizedType) ltype;
            Type subtype = type.getActualTypeArguments()[0];
            for (Object item : list) {
                victims.add(toUserObject(subtype, (List) item));
            }
        }
        return victims;
    }

    // --------------------------------------------------------------------------------------
    // From DBCursor to List:
    // --------------------------------------------------------------------------------------

    public static <T extends Object> List<T> toList(Class<T> type,
            DBCursor victims) {
        if (victims != null) {
            List<T> values = new ArrayList<>();
            while (victims.hasNext()) {
                T item = MongoUtil.toUserObject(type, victims.next());
                if (item != null) {
                    values.add(item);
                }
            }
            return values;
        }
        return null;
    }

    // --------------------------------------------------------------------------------------
    // Mongo annotations:
    // --------------------------------------------------------------------------------------

    public static boolean isMongoClass(Class<?> type) {
        return type.isAnnotationPresent(MongoClass.class);
    }

    public static MongoField getMongoField(Class<?> type) {
        return type.getAnnotation(MongoField.class);
    }

    public static MongoField getMongoField(Field field) {
        return field.getAnnotation(MongoField.class);
    }
}
