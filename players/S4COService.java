//import javax.jws.WebService;

//@WebService(name="S4CO", targetNamespace="http://ws.apache.org/axis2", serviceName="S4COService")
public class S4COService { 
	
 
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