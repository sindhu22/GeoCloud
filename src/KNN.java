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

public class KNN {

    public static class Map extends Mapper <LongWritable, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private double x;
        private double y;
        private final static ArrayList<Point>    ListOfPoints = new ArrayList<Point>();

        public void map(LongWritable key, Text value, Context context) 
            throws IOException, InterruptedException {

                // Get values of K , X and Y
                Configuration conf = context.getConfiguration();
                Double  givenX = (double)Float.parseFloat(conf.get("x"));
                Double  givenY = (double)Float.parseFloat(conf.get("y"));

                // read input points
                String line = value.toString();
                StringTokenizer tokenizer = new StringTokenizer(line);
                while(tokenizer.hasMoreTokens()){
                    x = Double.parseDouble(tokenizer.nextToken());
                    y = Double.parseDouble(tokenizer.nextToken());
                    ListOfPoints.add(new Point(x,y));
                }

                for(Point p1 : ListOfPoints){
                    ArrayList<Distance> ListOfDist = new ArrayList<Distance>();
                    for(Point p2 : ListOfPoints) {
                        if(p1 != p2){
                            // find nearest neighbor
                            ListOfDist.add(new Distance(p1, p2));
                        }
                    }
                    Collections.sort(ListOfDist);
                    if(ListOfDist.get(0).GetRight() == new Point(givenX, givenY)){
                        // emit the point
                    }
                }
            }
    }

    public static class Reduce extends Reducer <Text, IntWritable, Text, IntWritable> {
        ArrayList <Distance> ListOfDist = new ArrayList<Distance>();

        public void reduce(Text key, Iterable<IntWritable> values, Context context) 
            throws IOException, InterruptedException {

                // just emit points
            }
    }

    public static void main(String[] args) 
        throws Exception {
            Configuration conf = new Configuration();

            conf.setFloat("x", Float.parseFloat(args[1]));
            conf.setFloat("y", Float.parseFloat(args[2]));

            Job job = new Job(conf, "knn");

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

