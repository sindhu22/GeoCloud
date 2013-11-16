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

            job.setMapOutputValueClass(Vector2SF.class);
            job.setOutputValueClass(SparseVector.class);

            job.setInputFormatClass(ARFFInputformat.class);
            job.setOutputFormatClass(ARFFOutputFormat.class);

            FileInputFormat.addInputPath(job, new Path(args[0]));
            Path out = new Path(args[1]);

            FileSystem.get(conf).delete(out, true);
            FileOutputFormat.setOutputPath(job, out);
            
            int res = job.waitForCompletion(true);
        }

        return 0;
    }
}

