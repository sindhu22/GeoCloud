package rnn;

import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;
import java.util.StringTokenizer;

import java.util.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class RNN {

    public static class Map extends Mapper <LongWritable, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private double x;
        private double y;
        private final static ArrayList<Distance> ListOfDist = new ArrayList<Distance>();

        public void map(LongWritable key, Text value, Context context) 
            throws IOException, InterruptedException {

                // Get values of K , X and Y
                Configuration conf = context.getConfiguration();
                Integer givenK = Integer.parseInt(conf.get("k"));
                Double  givenX = (double)Float.parseFloat(conf.get("x"));
                Double  givenY = (double)Float.parseFloat(conf.get("y"));

                // read input points
                String line = value.toString();
                StringTokenizer tokenizer = new StringTokenizer(line);
                while(tokenizer.hasMoreTokens()){
                    x = Double.parseDouble(tokenizer.nextToken());
                    y = Double.parseDouble(tokenizer.nextToken());
                    Point p1 = new Point(x,y);
                    Point p2 = new Point(givenX, givenY);
                    ListOfDist.add(new Distance(p1, p2));
                }

                Collections.sort(ListOfDist);
                Iterator<Distance> dI = ListOfDist.iterator();

                for(int numPts = 0; (numPts < givenK) && (dI.hasNext()); numPts++){
                    // emit the point
                    Distance d = dI.next();
                    Point p = d.GetLeft();
                    // emit code here
                }
            }
    }

    public static class Reduce extends Reducer <Text, IntWritable, Text, IntWritable> {
        ArrayList <Distance> ListOfDist = new ArrayList<Distance>();

        public void reduce(Text key, Iterable<IntWritable> values, Context context) 
            throws IOException, InterruptedException {
                Configuration conf = context.getConfiguration();
                Integer givenK = Integer.parseInt(conf.get("k"));
                Double  givenX = (double)Float.parseFloat(conf.get("x"));
                Double  givenY = (double)Float.parseFloat(conf.get("y"));

                // read k mapped points
                while(values.hasNext()){
                    ListOfDist.add(new Distance(values.next(), new Point(givenX, givenY)));
                }

                Collections.sort(ListOfDist);

                Iterator<Distance> dI = ListOfDist.iterator();

                for(int numPts = 0; (numPts < givenK) && (dI.hasNext()); numPts++){
                    // emit the point
                    Distance d = dI.next();
                    Point p = d.GetLeft();
                    // emit code here
                }
            }
    }

    public static void main(String[] args) 
        throws Exception {
            Configuration conf = new Configuration();

            conf.setInt("k", Integer.parseInt(args[0]));
            conf.setFloat("x", Float.parseFloat(args[1]));
            conf.setFloat("y", Float.parseFloat(args[2]));

            Job job = new Job(conf, "rnn");

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);

            job.setMapperClass(Map.class);
            job.setReducerClass(Reducer.class);

            job.setInputFormatClass(TextInputFormat.class);
            job.setOutputFormatClass(TextOutputFormat.class);

            FileInputFormat.addInputPath(job, new Path(args[3]));
            FileOutputFormat.setOutputPath(job, new Path(args[4]));

            job.waitForCompletion(true);
        }
}

