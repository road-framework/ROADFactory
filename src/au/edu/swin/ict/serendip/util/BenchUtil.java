package au.edu.swin.ict.serendip.util;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.Role;
import au.edu.swin.ict.serendip.core.ModelProviderFactory;

public class BenchUtil {
    public static boolean BENCH_ON = false;
    private static Logger log = Logger.getLogger(BenchUtil.class.getName());
    private FileWriter writer = null;
    private static String path = null;// System.getenv("AXIS2_HOME")+System.getProperty("file.separator")+"test.csv";
    private long begintime = System.currentTimeMillis();

    public BenchUtil(String name) {
	try {
	    String benchStr = ModelProviderFactory.getProperty("BENCH_ON");
	    if (null != benchStr) {
		if (benchStr.equalsIgnoreCase("true")) {
		    BENCH_ON = true;
		} else {
		    BENCH_ON = false;
		    log.info("Bench mark OFF");
		}
	    } else {
		log.info("Bench mark OFF");
	    }
	    path = System.getenv("AXIS2_HOME")
		    + System.getProperty("file.separator") + name + ".csv";
	    writer = new FileWriter(path);
	    writer.append("Key, Property, Timestamp \n");
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    private long getElapsedTime() {
	return (System.currentTimeMillis() - begintime);
    }

    public void addBenchRecord(String key, String property) {
	if (!BENCH_ON) {
	    return;
	}

	try {
	    if (null != writer) {
		long time = getElapsedTime();
		String str = key + "," + property + "," + time + "\n";
		writer.append(str);
		log.info("::::::" + str);
	    } else {
		System.out.println("ERROR: CSV file is null " + path);
		log.info("ERROR: CSV file is null " + path);
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void finalize() {
	if (null != writer) {
	    try {
		writer.flush();
		writer.close();
		log.info("CSV written to " + path);
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	} else {
	    System.out.println("ERROR: CSV file is null");
	}
    }
}
