//------------------------------------------------------------------------------------------
// SGDI, Práctica 3, Grupo 3
// Rotaru, Dan Cristian
// Suárez García, Gorka
//
// Declaración de integridad: Ámbos dos declaramos que el código del proyecto
// es fruto exclusivamente del trabajo de sus miembros.
//------------------------------------------------------------------------------------------
package sgdi.pr3.grupo03.shared;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleUtil {
    // --------------------------------------------------------------------------------------
    // Constants:
    // --------------------------------------------------------------------------------------

    public static final int DEFAULT_INT = 0;
    public static final String DEFAULT_STRING = "";
    public static final String DEFAULT_OFFSET = "    ";

    // --------------------------------------------------------------------------------------
    // Basic input:
    // --------------------------------------------------------------------------------------

    public static Object getDefaultValue(Class<?> type) {
        if (type == String.class) {
            return DEFAULT_STRING;
        } else if (type == int.class) {
            return DEFAULT_INT;
        }
        return null;
    }

    public static Object getValue(Class<?> type, Object defaultValue) {
        Scanner scanner = new Scanner(System.in);
        try {
            if (type == String.class) {
                return scanner.nextLine();
            } else if (type == int.class) {
                return scanner.nextInt();
            }
        } catch (Exception ex) {
        }
        return defaultValue;
    }

    public static Object getValue(Class<?> type) {
        return getValue(type, getDefaultValue(type));
    }

    public static int getInt() {
        return (int) getValue(int.class, DEFAULT_INT);
    }

    public static String getString() {
        return (String) getValue(String.class, DEFAULT_STRING);
    }

    public static int getInt(int defaultValue) {
        return (int) getValue(int.class, defaultValue);
    }

    public static String getString(String defaultValue) {
        return (String) getValue(String.class, defaultValue);
    }

    // --------------------------------------------------------------------------------------
    // Build objects:
    // --------------------------------------------------------------------------------------

    public static <T extends Object> T getObject(Class<T> type,
            CustomReferenceFieldInput refFieldInput)
            throws NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        return getObject(type, refFieldInput, "");
    }

    public static <T extends Object> T getObject(Class<T> type,
            CustomReferenceFieldInput refFieldInput, String offset)
            throws NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        if (type == null)
            return null;
        T object = type.getConstructor().newInstance();
        Field[] fields = type.getDeclaredFields();
        int idx = 0, total = fields.length - getIgnoredFields(fields);
        for (Field field : fields) {
            if (!isIgnoreInputField(field)) {
                ++idx;
                field.setAccessible(true);
                System.out.println(offset + "[" + idx + "/" + total + "] "
                        + getOutputText(field));

                Object fvalue = null;
                Class<?> ftype = field.getType();
                ReferenceFieldType rft = getReferenceFieldType(field);
                if (rft != ReferenceFieldType.None) {
                    if (refFieldInput != null) {
                        fvalue = refFieldInput.getReference(rft, offset
                                + DEFAULT_OFFSET);
                        if (fvalue == null) {
                            field.setAccessible(false);
                            if (ftype == List.class) {
                                return null;
                            }
                            return object;
                        }
                    } else {
                        field.setAccessible(false);
                        continue;
                    }
                } else {
                    if (ftype == List.class) {
                        fvalue = getList(
                                ((ParameterizedType) field.getGenericType())
                                        .getActualTypeArguments()[0],
                                refFieldInput, offset + DEFAULT_OFFSET);

                    } else if (ftype.isPrimitive() || ftype == String.class) {
                        System.out.print(offset + "? ");
                        fvalue = getValue(ftype);

                    } else {
                        fvalue = getObject(ftype, refFieldInput, offset
                                + DEFAULT_OFFSET);
                    }
                }

                if (fvalue != null) {
                    field.set(object, fvalue);
                } else if (rft == ReferenceFieldType.None
                        || refFieldInput != null) {
                    System.err.println("Valor nulo en el campo: "
                            + field.getName() + " -> " + ftype.getName());
                }
                field.setAccessible(false);
            }
        }
        return object;
    }

    public static List getList(Type ltype,
            CustomReferenceFieldInput refFieldInput, String offset)
            throws NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        if (ltype == null)
            return null;
        List victims = new ArrayList();

        int listOfWhat;
        final int LPRIMITIVE = 1;
        final int LOBJECT = 2;
        final int LLIST = 3;
        Class<?> ctype = null;
        Type ptype = null;
        if (ltype instanceof Class<?>) {
            ctype = (Class<?>) ltype;
            if (ctype.isPrimitive() || ctype == String.class) {
                listOfWhat = LPRIMITIVE;
            } else {
                listOfWhat = LOBJECT;
            }
        } else {
            ptype = ((ParameterizedType) ltype).getActualTypeArguments()[0];
            listOfWhat = LLIST;
        }

        String input;
        boolean exit;
        int idx = 0;
        if (listOfWhat == LPRIMITIVE) {
            System.out.println(offset
                    + "Añadir elementos a la lista (. para terminar)");
        }
        do {
            if (listOfWhat == LOBJECT || listOfWhat == LLIST) {
                System.out.println(offset
                        + "¿Añadir un objeto a la lista? (. para terminar)");
            }
            System.out.print(offset + ++idx + " ? ");
            input = getString();
            exit = input.equals(".");
            if (!exit) {
                if (listOfWhat == LPRIMITIVE) {
                    if (ctype == String.class) {
                        victims.add(input);
                    } else {
                        try {
                            victims.add(Integer.parseInt(input));
                        } catch (Exception ex) {
                            victims.add(0);
                        }
                    }
                } else if (listOfWhat == LOBJECT) {
                    Object object = getObject(ctype, refFieldInput, offset
                            + DEFAULT_OFFSET);
                    if (object != null) {
                        victims.add(object);
                    }
                } else if (listOfWhat == LLIST) {
                    victims.add(getList(ptype, refFieldInput, offset
                            + DEFAULT_OFFSET));
                }
            }
        } while (!exit);
        return victims;
    }

    private static int getIgnoredFields(Field[] fields) {
        int total = 0;
        for (Field field : fields) {
            if (isIgnoreInputField(field)) {
                total++;
            }
        }
        return total;
    }

    public static <T extends Object> T modifyObject(T object, Class<T> clazz)
            throws NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        return modifyObject(object, null, clazz, "");
    }

    public static <T extends Object> T modifyObject(T object,
            CustomReferenceFieldInput input, Class<T> clazz)
            throws NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        return modifyObject(object, input, clazz, "");
    }

    public static <T extends Object> T modifyObject(T object,
            CustomReferenceFieldInput refInput, Class<T> type, String offset)
            throws NoSuchMethodException, SecurityException,
            InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        if (type == null)
            return null;
        Field[] fields = type.getDeclaredFields();

        int idx = 0, total = fields.length - getIgnoredFields(fields);
        for (Field field : fields) {
            if (!isIgnoreInputField(field)) {
                ReferenceFieldType rft = getReferenceFieldType(field);
                if (rft != ReferenceFieldType.None) {
                    continue;
                }
                ++idx;
                field.setAccessible(true);

                String fieldName = getOutputText(field);
                System.out.println(offset + "[" + idx + "/" + total + "] "
                        + fieldName);
                Object fieldValue = field.get(object);

                String currentValue = displayValue(fieldValue);

                System.out.println(offset + "Valor actual: " + currentValue);
                System.out
                        .println("Pulsa . para saltar este campo, introduce cualquier otro texto para modificarlo.");

                String input = getString(".");
                if (input.equals(".")) {
                    continue;
                }
                System.out.println("Modificando " + fieldName + "...");

                Object newFvalue = null;
                Class<?> ftype = field.getType();
                if (ftype == List.class) {
                    newFvalue = getList(
                            ((ParameterizedType) field.getGenericType())
                                    .getActualTypeArguments()[0],
                            refInput, offset + DEFAULT_OFFSET);

                } else if (ftype.isPrimitive() || ftype == String.class) {
                    System.out.print(offset + "? ");
                    newFvalue = getValue(ftype);

                } else {
                    newFvalue = getObject(ftype, refInput, offset
                            + DEFAULT_OFFSET);
                }

                if (newFvalue != null) {
                    field.set(object, newFvalue);
                } else {
                    System.err.println("Valor nulo en el campo: "
                            + field.getName() + " -> " + type.getName());
                }
                field.setAccessible(false);
            }
        }
        return (T) object;
    }

    private static String displayValue(Object object)
            throws IllegalArgumentException, IllegalAccessException {
        if (object instanceof List) {
            return displayList((List) object);
        } else if (object instanceof String) {
            return (String) object;
        } else if (object instanceof Integer) {
            return ((Integer) object).toString();
        } else {
            Field[] fields = object.getClass().getDeclaredFields();
            String msg = "{";
            int idx = 0, total = fields.length - getIgnoredFields(fields);
            for (Field field : fields) {
                if (!isIgnoreInputField(field)) {
                    ReferenceFieldType rft = getReferenceFieldType(field);
                    if (rft != ReferenceFieldType.None) {
                        continue;
                    }
                    ++idx;
                    field.setAccessible(true);
                    msg += getOutputText(field) + ": "
                            + displayValue(field.get(object));
                    if (idx < total) {
                        msg += ", ";
                    }
                    field.setAccessible(false);
                }
            }

            return msg + "}";
        }
    }

    private static String displayList(List list)
            throws IllegalArgumentException, IllegalAccessException {
        String msg = "[";
        if (list.isEmpty()) {
            msg += "lista vacía";
        } else {
            msg += displayValue(list.get(0));
            for (int i = 1, n = list.size(); i < n; ++i) {
                msg += ", " + displayValue(list.get(i));
            }
        }
        return msg + "]";
    }

    // --------------------------------------------------------------------------------------
    // Input annotations:
    // --------------------------------------------------------------------------------------
    public static String getOutputText(Object element) {
        if (element instanceof AnnotatedElement) {
            AnnotatedElement elem = (AnnotatedElement) element;
            if (elem.isAnnotationPresent(UserInputField.class)) {
                UserInputField uif = (UserInputField) elem
                        .getAnnotation(UserInputField.class);
                return uif.outputText();
            } else if (elem instanceof Field) {
                return ((Field) elem).getName();
            } else if (elem instanceof Class) {
                return ((Class) elem).getSimpleName();
            }
        } else if (element != null) {
            return element.toString();
        }
        return null;
    }

    public static boolean isIgnoreInputField(Field field) {
        return field.isAnnotationPresent(IgnoreInputField.class);
    }

    public static void drawMenu(String título, int returnInt,
            Object... opciones) {
        System.out.println("\n--" + título + " (" + returnInt
                + " para volver) --\n");
        for (int i = 0, n = opciones.length; i < n; ++i) {
            Object opcion = opciones[i];
            String mensajeOpcion;
            if (opcion instanceof AnnotatedElement) {
                mensajeOpcion = ConsoleUtil
                        .getOutputText((AnnotatedElement) opcion);
            } else {
                mensajeOpcion = opcion.toString();
            }
            System.out.println(i + "\t" + mensajeOpcion);
        }
        System.out.print("> ");
    }

    public static ReferenceFieldType getReferenceFieldType(Field field) {
        if (field != null && field.isAnnotationPresent(UserInputField.class)) {
            UserInputField uif = (UserInputField) field
                    .getAnnotation(UserInputField.class);
            return uif.referenceType();
        }
        return ReferenceFieldType.None;
    }

}
