public class S6COService { 
	
 
	public String tAnalyze(String content) { 
		
		String path = System.getenv("AXIS2_HOME")+System.getProperty("file.separator")+"s6co.txt";
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
		//TODO: Case officer need to determine the category of the player and then request for services. 
		String res = " I recieved "+content+". Need to book: taxi, repair, hotel";
		return res;
	}
 
	public String tPayGR(String content) {return "XXX";}
	public String tPayTT(String content) { return "XXX";	}
	public String tPlaceTaxiOrder(String content) { return "XXX";	}
	public String tPayTaxi(String content) { return "XXX"	;}
	public String tBookHotel(String content) { return "XXX"	;}
	public String tPayHotel(String content) { return "XXX"	;}
	public String tHandleException(String content) { return "XXX"	;}
 
	
	public String tNotify(String content) { 
		String res = "Dear, Pickup car from Southlink Smash Repairs - Dandenong";
		System.out.println(res);
		return res;
	}
}