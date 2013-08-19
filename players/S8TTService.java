 
import java.util.Random; 
public class S8TTService { 
	 
	public synchronized String orderTow(String content)   { 
		//Process Message
		try{
			System.out.println("Busy!");
 
			Thread.sleep(10000) ;
		}catch(Exception e){
			System.out.println(e);
		}
		String res = " Tow complete. Car is towed to  Southlink Smash Repairs - Dandenong";
		System.out.println(res);
 
		return res;
	}
}