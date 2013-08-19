//import javax.jws.WebService;

//@WebService(name="S4TTService", targetNamespace="http://ws.apache.org/axis2_tt", serviceName="S4TTService")
public class S6TTService { 
 
	public String tTow(String content) { 
		//Process Message
		String res = " Tow complete. Car is towed to  Southlink Smash Repairs - Dandenong";
		System.out.println(res);
		return res;
	}
	
	public void tAcceptPayment(String content) { 
		System.out.println("Payment accepted : TT");
	}
}