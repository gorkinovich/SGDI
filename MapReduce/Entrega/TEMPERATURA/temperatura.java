//******************************************************************************************
// SGDI, Práctica 1, Apartado 2: TEMPERATURA
// Dan Cristian Rotaru y Gorka Suárez García
//******************************************************************************************
import java.io.IOException;
import java.util.StringTokenizer;
import java.lang.String;
import java.lang.Float;
import java.lang.Math;

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
Ahora vamos a utilizar el fichero JCMB_last31days.csv que registra diversos parámetros
recogidos por una estación meteorológica en la Universidad de Edimburgo, obtenido de
http://www.ed.ac.uk/schools-departments/geosciences/weather-station/download-weather-
data, con 45.000 líneas. Estos datos han sido recogido minuto a minuto durante 31 días. El
formato del fichero es de valores separados por comas (CSV), con los siguientes 17 campos
en cada línea:

    index,year,day of the year,time,atmospheric pressure (mBar),
    rainfall (mm),wind speed (m/s),wind direction (degrees),
    surface temperature (C),relative humidity (%),wind_max (m/s),Tdew (C),
    wind_chill (C),uncalibrated solar flux (Kw/m2),
    calibrated solar flux (Kw/m2),battery (V),not used

Queremos procesar el fichero para conocer, para cada día, la diferencia máxima y mínima
entre la temperatura en superficie (surface temperature) y la sensación térmica (wind
chill).
*/
public class temperatura {
    public static class GenericMapper
        extends Mapper<Object, Text, Text, FloatWritable> {

        private final static FloatWritable val = new FloatWritable();
        private final static Text word = new Text();

        public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {
            String[] datos = value.toString().split(",");
            try {
                float diff = Float.valueOf(datos[8]) - Float.valueOf(datos[12]);
                word.set(datos[1] + " " + datos[2]);
                val.set(diff);
                context.write(word, val);
            } catch (NumberFormatException nfex) {
            }
        }
    }

    public static class GenericReducer
        extends Reducer<Text,FloatWritable,Text,Text> {

        private final static Text result = new Text();

        public void reduce(Text key, Iterable<FloatWritable> values, Context context)
            throws IOException, InterruptedException {
            float max = Float.MIN_VALUE, min = Float.MAX_VALUE;
            for (FloatWritable val : values) {
                float value = val.get();
                min = Math.min(min, value);
                max = Math.max(max, value);
            }
            result.set("Min: " + min + ", Max: " + max);
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        JobConf conf = new JobConf();
        Job job = Job.getInstance(conf);
        job.setJarByClass(temperatura.class);
        job.setMapperClass(GenericMapper.class);

        job.setReducerClass(GenericReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FloatWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);

        FileInputFormat.addInputPath(job, new Path("JCMB_last31days.csv"));
        FileOutputFormat.setOutputPath(job, new Path("salida"));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
