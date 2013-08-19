//import javax.jws.WebService;

//@WebService(name="S8CO", targetNamespace="http://ws.apache.org/axis2", serviceName="S8COService")
public class S8COService { 
	
 
	public String analyze(String content) { 
		String res = " Pickup from "+content;
		System.out.println(res);
		return res;
	}
 
	public String notify(String content) { 
		String res = " Car is towed to. Southlink Smash Repairs - Dandenong";
		System.out.println(res);
		return res;
	}
}