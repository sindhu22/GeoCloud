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

import com.infomatiq.jsi.rtree.*;
import com.infomatiq.jsi.*;

import gnu.trove.*;

public class KNNRTree {

    public static ArrayList<Integer> kNNPoints;

    private static class NullProc implements TIntProcedure {
        public boolean execute(int n){
            return true;
        }
    }

    private static class EmitProc implements TIntProcedure {
        public boolean execute(int n){
            kNNPoints.add(n);
            return true;
        }
    }

    public static class KNNRTreeMap extends Mapper <LongWritable, Text, Text, Text> {
        public void map(LongWritable key, Text value, Context context) 
            throws IOException, InterruptedException {

            Configuration conf = context.getConfiguration();
            Integer givenK = Integer.parseInt(conf.get("k"));
            Float  givenX = Float.parseFloat(conf.get("x"));
            Float  givenY = Float.parseFloat(conf.get("y"));
            // System.out.println("Emitting point: " + (givenX.toString() + " " + givenY.toString()) + " " + value.toString());
            context.write(new Text(givenX.toString() + " " + givenY.toString()), value);
        }
    }

    public static class KNNRTreeReduce extends Reducer <Text, Text, Text, Text> {
        private float x;
        private float y;

        private static ArrayList<Point> ListOfPoints;
        private static Integer pointID;

        public void reduce(Text key, Iterable<Text> values, Context context) 
            throws IOException, InterruptedException {


            Configuration conf = context.getConfiguration();
            Integer givenK = Integer.parseInt(conf.get("k"));
            Float  givenX = Float.parseFloat(conf.get("x"));
            Float  givenY = Float.parseFloat(conf.get("y"));

            SpatialIndex gKNNRTree = new RTree();
            gKNNRTree.init(null);

            ListOfPoints = new ArrayList<Point>();
            kNNPoints = new ArrayList<Integer>();
            pointID = new Integer(0);

            for(Text value : values){
                String line = value.toString();
                StringTokenizer tokenizer = new StringTokenizer(line);
                x = Float.parseFloat(tokenizer.nextToken());
                y = Float.parseFloat(tokenizer.nextToken());
                ListOfPoints.add(new Point(x,y));
                if(gKNNRTree.equals(null)){
                    System.out.println("Rtree not initialized");
                }
                else {
                    gKNNRTree.add(new Rectangle(x,y,x,y), pointID++);
                }
            }

            TIntProcedure proc = new EmitProc();
            gKNNRTree.nearestN(new Point(givenX, givenY), proc, givenK, Float.MAX_VALUE);

            for(int i : kNNPoints){
                context.write(new Text(new Point(givenX, givenY).toString()), new Text(ListOfPoints.get(i).toString()));
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

        Job job = new Job(conf, "knnrtree");

        job.setJarByClass(KNNRTree.class);
        job.setNumReduceTasks(1);

        //job.setMapOutputKeyClass(Text.class);
        //job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setMapperClass(KNNRTreeMap.class);
        //job.setCombinerClass(KNNRTreeReduce.class);
        job.setReducerClass(KNNRTreeReduce.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[3]));
        FileOutputFormat.setOutputPath(job, new Path(args[4]));

        job.waitForCompletion(true);

        final long endTime = System.currentTimeMillis();
        System.out.println("Elapsed Time: " + (endTime - startTime));

    }
}

