//import javax.jws.WebService;
import java.util.Random;
//@WebService(name="S8TTService", targetNamespace="http://ws.apache.org/axis2_tt", serviceName="S8TTService")
public class S8TTService2 { 
	public static boolean isBusy = false;
	public synchronized String orderTow(String content) { 
		//Process Message
		try{
			System.out.println("Busy!");
			//Random randomGenerator = new Random();
			//int factor = randomGenerator.nextInt(9)+1;
			isBusy = true;
			Thread.sleep(10000);//* factor : random delay factors to simulate the different towing conditions
		}catch(Exception e){
			System.out.println(e);
		}
		String res = " Tow complete. Car is towed to  Southlink Smash Repairs - Dandenong";
		System.out.println(res);
		isBusy = false;
		return res;
	}
}