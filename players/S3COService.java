//import javax.jws.WebService;

//@WebService(name="S4CO", targetNamespace="http://ws.apache.org/axis2", serviceName="S4COService")
public class S3COService { 
	
 
	public String analyze(String content) { 
		
		String path = System.getenv("AXIS2_HOME")+System.getProperty("file.separator")+"s3co.txt";
		while( !(new java.io.File(path)).exists())
        {			
                try {
                        Thread.sleep(1000);
                } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        return null;
                }
        }
		
		String res = " I recieved "+content+" I booked, $Hotel, $Taxi, $Tow, $Repair";
		return res;
	}
 
	public String notify(String content) { 
		String res = "Dear, Pickup car from Southlink Smash Repairs - Dandenong";
		System.out.println(res);
		return res;
	}
}