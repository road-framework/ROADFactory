 
public class S6GRService { 
 
	public String tAcceptRepairOrder(String content) { 
		//Process Message
		String res = " Repair accepted. Southlink Smash Repairs - Dandenong";
		System.out.println(res);
		return res;
	}
	
	public String tDoRepair(String content) { 
		String res = " Repair complete. Southlink Smash Repairs - Dandenong";
		System.out.println(res);
		return res;
	}
	public void tAcceptPayment(String content) { 
		System.out.println("Payment accepted : GR");
	}
	
}