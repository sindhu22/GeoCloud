
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

    public static class RNNMap extends Mapper <LongWritable, Text, Text, Text> {
        private double x;
        private double y;
        private final static ArrayList<Point>    ListOfPoints = new ArrayList<Point>();

        public void map(LongWritable key, Text value, Context context) 
            throws IOException, InterruptedException {

            // Get values of X and Y
            Configuration conf = context.getConfiguration();
            Double  givenX = Double.parseDouble(conf.get("x"));
            Double  givenY = Double.parseDouble(conf.get("y"));

            System.out.println("Emitting: " + value.toString());
            context.write(new Text(new Point(givenX, givenY).toString()), value);

            // read input points
        }
    }

    public static class RNNReduce extends Reducer <Text, Text, Text, Text> {
        private double x;
        private double y;
        ArrayList<Point>	ListOfPoints = new ArrayList<Point>();

        public void reduce(Text key, Iterable<Text> values, Context context) 
            throws IOException, InterruptedException {

            Configuration conf = context.getConfiguration();
            Double  givenX = Double.parseDouble(conf.get("x"));
            Double  givenY = Double.parseDouble(conf.get("y"));
            Point GivenPoint = new Point(givenX, givenY);

            for(Text value : values){
                String line = value.toString();
                StringTokenizer tokenizer = new StringTokenizer(line);
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
                Point NN = ListOfDist.get(0).GetRight();
                if(NN.toString().equals(GivenPoint.toString())){
                    // emit the point
                    context.write(new Text(p1.toString()), new Text(GivenPoint.toString()));
                }
            }
        }
    }

    public static void main(String[] args) 
        throws Exception {
        Configuration conf = new Configuration();

        conf.set("x", (args[0]));
        conf.set("y", (args[1]));

        Job job = new Job(conf, "rnn");

        job.setJarByClass(RNN.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setMapperClass(RNNMap.class);
        //job.setCombinerClass(RNNReduce.class);
        job.setReducerClass(RNNReduce.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);


        FileInputFormat.addInputPath(job, new Path(args[2]));
        FileOutputFormat.setOutputPath(job, new Path(args[3]));

        job.waitForCompletion(true);
    }
}

