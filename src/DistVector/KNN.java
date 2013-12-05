
/* This class implements K nearest neighbor algorithm for given dataset
   using distance vector method using map reduce paradigm.
 */

import java.util.*;
/* Authors: Abhijeet Nayak,
   	    Dhananjay Bhirud,
	    Kumar Sadhu,
	    Sindhu Suryanayana
 */
import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class KNN {

    public static class KNNMap extends Mapper <LongWritable, Text, Text, Text> {
        private double x;
        private double y;

        public void map(LongWritable key, Text value, Context context) 
            throws IOException, InterruptedException {
            ArrayList<Distance> ListOfDist = new ArrayList<Distance>();

            // Get values of K , X and Y
            Configuration conf = context.getConfiguration();
            Integer givenK = Integer.parseInt(conf.get("k"));
            Double  givenX = Double.parseDouble(conf.get("x"));
            Double  givenY = Double.parseDouble(conf.get("y"));

            // read input points
            // System.out.println("Emitting: " + value.toString());
            context.write(new Text(new Point(givenX, givenY).toString()), value);
        }
    }

    public static class KNNReduce extends Reducer <Text, Text, Text, Text> {
        private double x;
        private double y;
        ArrayList <Distance> ListOfDist = new ArrayList<Distance>();

        public void reduce(Text key, Iterable<Text> values, Context context) 
            throws IOException, InterruptedException {

            Configuration conf = context.getConfiguration();
            Integer givenK = Integer.parseInt(conf.get("k"));
            Double  givenX = (double)Float.parseFloat(conf.get("x"));
            Double  givenY = (double)Float.parseFloat(conf.get("y"));

            // read k mapped points
            for(Text value : values){
                String line = value.toString();
                StringTokenizer tokenizer = new StringTokenizer(line);
                x = Double.parseDouble(tokenizer.nextToken());
                y = Double.parseDouble(tokenizer.nextToken());
                Point p1 = new Point(x,y);
                Point p2 = new Point(givenX, givenY);
                ListOfDist.add(new Distance(p1,p2));
                // System.out.println(new Distance(p1,p2).toString());
            }

            Collections.sort(ListOfDist);

            Iterator<Distance> dI = ListOfDist.iterator();

            for(int numPts = 0; (numPts < givenK) && (dI.hasNext()); numPts++){
                Distance d = dI.next();
                Point p = d.GetLeft();
                // emit the point
                context.write(new Text(new Point(givenX, givenY).toString()), new Text(p.toString()));
            }
        }
    }

    public static void main(String[] args) 
        throws Exception {

        final long startTime = System.currentTimeMillis();

        Configuration conf = new Configuration();

        conf.setInt("k", Integer.parseInt(args[0]));
        conf.setFloat("x", Float.parseFloat(args[1]));
        conf.setFloat("y", Float.parseFloat(args[2]));

        Job job = new Job(conf, "knn");

        job.setJarByClass(KNN.class);
        job.setNumReduceTasks(1);

        //job.setMapOutputKeyClass(Text.class);
        //job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setMapperClass(KNNMap.class);
        //job.setCombinerClass(KNNReduce.class);
        job.setReducerClass(KNNReduce.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[3]));
        FileOutputFormat.setOutputPath(job, new Path(args[4]));

        job.waitForCompletion(true);
        final long endTime = System.currentTimeMillis();
        System.out.println("Elapsed time: " + (endTime - startTime));
    }
}

