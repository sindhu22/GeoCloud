import java.io.*;
import java.util.*;


import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import org.apache.hadoop.mapreduce.Reducer;




public class KnnDriver extends Configured implements Tool
{

    public static void main(String[] args) throws Exception
    {
        int res = ToolRunner.run(new Configuration(), new KnnDriver(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception
    {
        
        Configuration conf = getConf();
        conf.set("seperate mapreduce output", ",");
        
        for (FileStatus fs : FileSystem.get(conf).listStatus(new Path(args[2])))
        {
            conf.set("test", fs.getPath().toString());

            
            Job job = new Job(conf, "Knn Classifier");
            job.setJarByClass(KnnDriver.class);

            job.setMapperClass(KnnMapper.class);
            job.setReducerClass(KnnReducer.class);
            job.setCombinerClass(KnnCombiner.class);

            job.setOutputKeyClass(Text.class);

            job.setMapOutputValueClass(Vector.class);
            job.setOutputValueClass(Vector.class);

            job.setInputFormatClass(ARFFInputformat.class);
            job.setOutputFormatClass(ARFFOutputFormat.class);

            FileInputFormat.addInputPath(job, new Path(args[0]));
            Path out = new Path(args[1]);

            FileSystem.get(conf).delete(out, true);
            FileOutputFormat.setOutputPath(job, out);
            
            int res = job.waitForCompletion(true) ? 0 : 1;
            if (res != 0)
            {
                return res;
            }
        }

        return 0;
    }
}

class KnnMapper extends Mapper<Text, Vector, Text, Vector>
{

    private Vector<Vector<String, Vector>> test = new Vector<Vector<String, Vector>>();

    protected void map(Text key, Vector value, org.apache.hadoop.mapreduce.Mapper<Text, Vector, Text, Vector>.Context context)
        throws java.io.IOException, InterruptedException
    {
        context.setStatus(key.toString());
        for (Vector<String, Vector> testCase : test) 
        {
            double d = testCase.getV2().dotProduct(value);
            context.write(new Text(testCase.getV1()), new Vector(key.toString(), (float) d));
        }

    }

    protected void setup(org.apache.hadoop.mapreduce.Mapper<Text, Vector, Text, Vector>.Context context)
            throws java.io.IOException, InterruptedException
            {

                FileSystem fs = FileSystem.get(context.getConfiguration());
                BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(new Path(context.getConfiguration().get("test", "test.arff")))));
                String line = br.readLine();
                int count = 0;
                while (line != null)
                {
                    Vector<String, Vector> v = ARFFInputformat.readLine(count, line);
                    test.add(new Vector<String, Vector>(v.getV1(), v.getV2()));
                    line = br.readLine();
                    count++;
                }
        br.close();
        System.out.println("done.");
    };

}

class KnnReducer extends Reducer<Text, Vector, Text, Vector> 
{

    protected void reduce(Text key, java.lang.Iterable<Vector> value, org.apache.hadoop.mapreduce.Reducer<Text, Vector, Text, Vector>.Context context)
        throws java.io.IOException, InterruptedException
        {
            ArrayList<Vector> vs = new ArrayList<Vector>();
        
            for (Vector v : value)
            {
                vs.add(new Vector(v.getV1(), v.getV2()));
            }
            Collections.sort(vs);

            int k = context.getConfiguration().getInt("knn", 4);
            Vector sp = new Vector();

            for (int i = 0; i < k && i < vs.size(); i++)
            {
                sp.put(vs.get(i).getV1(), vs.get(i).getV2());
            }
            context.write(key, sp);
        }
}

class KNNCombiner extends Reducer<Text, Vector, Text, Vector> 
{
        protected void reduce(Text key, java.lang.Iterable<Vector> value, org.apache.hadoop.mapreduce.Reducer<Text, Vector, Text, Vector>.Context context)
        throws java.io.IOException, InterruptedException
        {
                ArrayList<Vector> vs = new ArrayList<Vector>();
                
                for (Vector v : value)
                {
                        vs.add(new Vector(v.getV1(), v.getV2()));
                }
                
                Collections.sort(vs);
                int k = context.getConfiguration().getInt("knn", 4);

                for (int i = 0; i < k && i < vs.size(); i++)
                {
                        context.write(key, vs.get(i));
                }
        };
}
