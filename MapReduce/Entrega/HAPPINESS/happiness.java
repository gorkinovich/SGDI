//******************************************************************************************
// SGDI, Práctica 1, Apartado 3: HAPPINESS
// Dan Cristian Rotaru y Gorka Suárez García
//******************************************************************************************
import java.io.IOException;
import java.util.StringTokenizer;
import java.lang.String;
import java.lang.Float;
import java.lang.Math;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
Ahora utilizaremos el fichero happiness.txt, que contiene una valoración de “felicidad”
para más de 10.000 palabras inglesas. Este fichero ha sido obtenido del artículo
Temporal
patterns of happiness and information in a global social network: Hedonometrics and
Twitter

http://arxiv.org/abs/1101.5120

El archivo está separado por tabuladores, donde cada
línea sigue el siguiente formato:
    word
    happiness_rank happiness_average happiness_standard_deviation
    twitter_rank google_rank nyt_rank lyrics_rank
Calcular aquellas palabras extremadamente tristes, es decir, aquellas cuya felicidad media
(happiness_average) está por debajo de 2 y que además tienen ranking de Twitter
(twitter_rank es diferente de --). La tarea MapReduce debe devolver una sola línea (es
decir, la fase REDUCE debe emitir una sola tupla) con el número de palabras
extremadamente tristes seguida de la lista de dichas palabras.
Ejemplo de salida
    3	“pena, tristeza, enfermedad”
*/
public class happiness {
    public static class GenericMapper
        extends Mapper<Object, Text, Text, Text> {

        private final static Text outKey = new Text("-");

        public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {
            String[] data = value.toString().split("\t");
            try {
                float happiness = Float.valueOf(data[2]);
                if(happiness < 2 && !data[4].equals("--")) {
                    value.set(data[0]);
                    context.write(outKey, value);
                }
            } catch (Exception nfex){
            }
        }
    }

    public static class GenericReducer
        extends Reducer<Text, Text, Text, Text> {

        private final static Text result = new Text();

        public void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
            int count = 0;
            String words = "";
            for (Text val : values) {
                if(count > 0) {
                    words += ", ";
                }
                words += val.toString();
                ++count;
            }
            key.set(String.valueOf(count));
            result.set(words);
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        JobConf conf = new JobConf();
        Job job = Job.getInstance(conf);
	   	job.setJarByClass(happiness.class);
        job.setMapperClass(GenericMapper.class);

        job.setReducerClass(GenericReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path("happiness.txt"));
        FileOutputFormat.setOutputPath(job, new Path("salida"));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
