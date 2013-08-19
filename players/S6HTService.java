public class S6HTService { 
 
	public String tConfirmBooking(String content) { 
		//Process Message
		String res = " Hotel Booked. Riverside INN. Room 301";
		System.out.println(res);
		return res;
	}
	
	public void tAcceptPayment(String content) { 
		System.out.println("Payment accepted : HT");
	}
}