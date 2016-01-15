//******************************************************************************************
// SGDI, Práctica 1, Apartado 4: INDEX
// Dan Cristian Rotaru y Gorka Suárez García
//******************************************************************************************
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.NavigableSet;

import java.lang.String;
import java.lang.Float;
import java.lang.Math;

import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
Utilizando los archivos de texto plano “Adventures_of_Huckleberry_Finn.txt”, “Hamlet.txt”
y “Moby_Dick.txt”, generar un índice inverso de palabras y archivos en los que aparecen.
No se debe hacer distinción entre mayúsculas y minúsculas, y las palabras no deben
contener signos de puntuación. El índice no debe contener todas las palabras que aparecen
en los libros, únicamente aquellas que sean populares, es decir, que aparecen más de 20
veces en alguno	de los libros. A cada palabra popular del índice le debe acompañar una
lista de parejas (libro, número de apariciones) ordenado por número de apariciones, y este
listado contendrá todos los libros donde aparece la palabra, incluidos aquellos donde
aparezca 20 veces o menos.
El resultado será un listado como el siguiente:
    ...
    "wind"
    "(Moby Dick.txt, 66), (Adventures of Huckleberry Finn.txt, 13)"
    "windlass"
    "(Moby Dick.txt, 21)"
    ...
Pista: será necesario obtener el nombre del fichero del que se ha obtenido la tupla, y este
valor no aparece en las claves ni los valores que se leen de los ficheros. Habrá que acceder
a ellos utilizando el entorno (Mrjob) o el argumento context(Hadoop).
*/
public class index {
    public static class GenericMapper
        extends Mapper<Object, Text, Text, Text> {

        private final static Text outKey = new Text();

        public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {
            String fileName = ((FileSplit) context.getInputSplit()).getPath().getName();
            String[] data = value.toString().split(" ");
            for(int i = 0, length = data.length; i < length; ++i) {
                String trimWord = data[i].replaceAll("[^a-zA-Z ]", "").toLowerCase();
                if(!trimWord.isEmpty()) {
                    outKey.set(trimWord);
                    value.set(fileName);
                    context.write(outKey, value);
                }
            }
        }
    }

    public static class GenericReducer
        extends Reducer<Text,Text,Text,Text> {

        private final static Text result = new Text();
        private final static String[] FILES = new String[] { "Moby_Dick.txt", "Hamlet.txt", "Adventures_of_Huckleberry_Finn.txt" };
        private final static Integer[] AMOUNTS = new Integer[3];
        private final static Integer MIN_APARITIONS = 20;
        private final static TreeMap<Integer, String> order = new TreeMap<Integer, String>();

        public void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {

            for(int i = 0, length = AMOUNTS.length; i < length; ++i) {
                AMOUNTS[i] = 0;
            }

            order.clear();

            for (Text val : values) {
                for(int i = 0, length = FILES.length; i < length; ++i) {
                    String currentFile = FILES[i];
                    if(currentFile.equals(val.toString())) {
                        AMOUNTS[i] = AMOUNTS[i] + 1;
                    }
                }
            }

            for(int i = 0, length = FILES.length; i < length; ++i) {
                Integer amount = AMOUNTS[i];
                if(amount > 0) {
                    String currentFile = FILES[i];
                    order.put(amount, currentFile);
                }
            }

            if(order.lastKey() > MIN_APARITIONS) {
                String res = "";
                NavigableSet<Integer> navig = order.descendingKeySet();
                int i = 0;
                Iterator<Integer> iter = navig.iterator();
                while (iter.hasNext()) {
                    Integer itrKey = iter.next();
                    if(i > 0) {
                        res += ", ";
                    }
                    res += "(" + order.get(itrKey) + ", " + itrKey + ")";
                    ++i;
                }
                result.set(res);
		      	context.write(key, result);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        JobConf conf = new JobConf();
        Job job = Job.getInstance(conf);
        job.setJarByClass(index.class);
        job.setMapperClass(GenericMapper.class);

        job.setReducerClass(GenericReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path("Moby_Dick.txt"));
        FileInputFormat.addInputPath(job, new Path("Hamlet.txt"));
        FileInputFormat.addInputPath(job, new Path("Adventures_of_Huckleberry_Finn.txt"));
        FileOutputFormat.setOutputPath(job, new Path("salida"));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
