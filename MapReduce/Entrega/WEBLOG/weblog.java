//******************************************************************************************
// SGDI, Práctica 1, Apartado 1: WEBLOG
// Dan Cristian Rotaru y Gorka Suárez García
//******************************************************************************************
import java.io.IOException;
import java.util.StringTokenizer;
import java.lang.String;
import java.lang.Integer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
Consideremos el log del servidor Web (weblog.txt, 47.778 líneas) que se puede encontrar en
el Campus Virtual, obtenido de http://ita.ee.lbl.gov/html/contrib/EPA-HTTP.html. Calcular el
número peticiones que han obtenido un código 302 cada día (no es necesario mostrar los
días en los que no se ha producido ninguna petición con código de respuesta 302). Las
líneas del log representan accesos al servidor, y están compuestas por los siguientes campos
separados por espacios:

    • host que realiza la petición (nombre de host o dirección IP)
    • fecha en el formato [DD:HH:MM:SS], donde DD es el día y HH:MM:SS es la hora.
    • Petición entre comillas. ¡Atención! La petición suele estar formada por 3 partes
      separadas por espacios: verbo HTTP (GET, POST, HEAD...), el recurso accedido y,
      opcionalmente, la versión de HTTP usada. El recurso accedido puede contener
      espacios y comillas.
    • Código HTTP de respuesta: 200, 404, 302...
    • Número de bytes de la contestación.

En la URL anterior se puede encontrar información más detallada.
*/
public class weblog {
    public static class GenericMapper
        extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private final static Text word = new Text();

        public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {
            String[] datos = value.toString().split(" ");
            try {
                if(Integer.valueOf(datos[datos.length - 2]) == 302) {
                    word.set(datos[1].substring(1, 3));
                    context.write(word, one);
                }
            } catch (NumberFormatException nfex) {
            }
        }
    }

    public static class GenericReducer
        extends Reducer<Text,IntWritable,Text,IntWritable> {

        private final static IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                ++sum;
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        JobConf conf = new JobConf();
        Job job = Job.getInstance(conf);
        job.setJarByClass(weblog.class);
        job.setMapperClass(GenericMapper.class);

        job.setReducerClass(GenericReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path("weblog.txt"));
        FileOutputFormat.setOutputPath(job, new Path("salida"));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
