package org.apache.rnn;

import java.io.IOException;
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

        public void map(LongWritable key, Text value, Context context) 
        throws IOException, InterruptedException {

            // read input points
            String line = value.toString();
            StringTokenizer tokenizer = new StringTokenizer(line);
            while(tokenizer.hasMoreTokens()){
                x = Double.parseDouble(tokenizer.nextToken());
                y = Double.parseDouble(tokenizer.nextToken());
                double dist = Math.sqrt((x*x) + (y*y));
                context.write(new Text(String.valueOf(dist)), one);
            }

            // emit the points
        }
    }

    public static class Reduce extends Reducer <Text, IntWritable, Text, IntWritable> {
        public void reduce(Text key, Iterable<IntWritable> values, Context context) 
        throws IOException, InterruptedException {
            double sum = 0.0;
            sum += Double.parseDouble(key.toString());
            context.write(new Text(String.valueOf(sum)), new IntWritable(1));
            // take map output
            // emit the result points
        }
    }

    public static void main(String[] args) 
    throws Exception {
        Configuration conf = new Configuration();
        Job job = new Job(conf, "rnn");

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(Map.class);
        job.setReducerClass(Reducer.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);
    }
}

