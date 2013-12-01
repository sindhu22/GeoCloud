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

public class RNNRTree {

    public static Integer pID;

    private static class NullProc implements TIntProcedure {
        public boolean execute(int n){
            return true;
        }
    }

    private static class EmitProc implements TIntProcedure {
	    public boolean execute(int n){
		    pID = n; 
		    return true;
	    }
    }


    public static class RNNRTreeMap extends Mapper <LongWritable, Text, Text, Text> {
        public void map(LongWritable key, Text value, Context context) 
            throws IOException, InterruptedException {

	    Configuration conf = context.getConfiguration();
            Float  givenX = Float.parseFloat(conf.get("x"));
            Float  givenY = Float.parseFloat(conf.get("y"));
	    // System.out.println("Emitting point: " + (givenX.toString() + " " + givenY.toString()) + " " + value.toString());
	    context.write(new Text(givenX.toString() + " " + givenY.toString()), value);
        }
    }

    public static class RNNRTreeReduce extends Reducer <Text, Text, Text, Text> {
        private float x;
        private float y;

	private static ArrayList<Point> ListOfPoints;
	private static Integer pointID;

        public void reduce(Text key, Iterable<Text> values, Context context) 
            throws IOException, InterruptedException {
		
            Configuration conf = context.getConfiguration();
            Float  givenX = Float.parseFloat(conf.get("x"));
            Float  givenY = Float.parseFloat(conf.get("y"));

	    SpatialIndex gRNNRTree = new RTree();
	    gRNNRTree.init(null);

	    ListOfPoints = new ArrayList<Point>();
	    pointID = new Integer(0);

	    for(Text value : values){
		    String line = value.toString();
		    StringTokenizer tokenizer = new StringTokenizer(line);
		    x = Float.parseFloat(tokenizer.nextToken());
		    y = Float.parseFloat(tokenizer.nextToken());
		    ListOfPoints.add(new Point(x,y));
		    if(gRNNRTree.equals(null)){
			    System.out.println("RTree not initialized");
		    }
		    else {
			    gRNNRTree.add(new Rectangle(x,y,x,y), pointID++);
		    }
	    }

	    TIntProcedure proc = new EmitProc();
	    for(Point p : ListOfPoints){
		    gRNNRTree.nearestN(p, proc, 2, Float.MAX_VALUE);
		    // System.out.println("p = " + p.toString());
		    // System.out.println("NN = " + ListOfPoints.get(pID).toString());
		    if(ListOfPoints.get(pID).toString().equals(new Point(givenX, givenY).toString())){
			    context.write(new Text(new Point(givenX, givenY).toString()), new Text(p.toString()));
		    }
	    }
        }
    }

    public static void main(String[] args) 
        throws Exception {

        final long startTime = System.currentTimeMillis();

        Configuration conf = new Configuration();
        conf.setFloat("x", Float.parseFloat(args[0]));
        conf.setFloat("y", Float.parseFloat(args[1]));

        Job job = new Job(conf, "rnnrtree");

        job.setJarByClass(RNNRTree.class);
        job.setNumReduceTasks(1);

        //job.setMapOutputKeyClass(Text.class);
        //job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setMapperClass(RNNRTreeMap.class);
        //job.setCombinerClass(RNNRTreeReduce.class);
        job.setReducerClass(RNNRTreeReduce.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[2]));
        FileOutputFormat.setOutputPath(job, new Path(args[3]));
	
        job.waitForCompletion(true);

        final long endTime = System.currentTimeMillis();
        System.out.println("Elapsed Time: " + (endTime - startTime));

    }
}

