public class S6TXService { 
 
	public String tProvideTaxi(String content) { 
		//Process Message
		String res = "Taxi-Done";
		System.out.println(res);
		return res;
	}
	public void tAcceptPayment(String content) { 
		System.out.println("Payment accepted : TX");
	}
}