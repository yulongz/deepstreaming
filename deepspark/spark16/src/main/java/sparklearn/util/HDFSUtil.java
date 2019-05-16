package sparklearn.util;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;

public class HDFSUtil {

	static private FileSystem local;
	static private FileSystem hdfs;

	static{
		Configuration conf = new Configuration();
		try {
			local = FileSystem.newInstanceLocal(conf);
			hdfs = DistributedFileSystem.get(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void hdfsdelDir(String path) throws Exception {
		if (hdfs.exists(new Path(path)) && hdfs.isDirectory(new Path(path)))
			hdfs.delete(new Path(path), true);
	}

	public static void localdelDir(String path) throws Exception {
		if (local.exists(new Path(path)) && local.isDirectory(new Path(path)))
			local.delete(new Path(path), true);
	}

}
