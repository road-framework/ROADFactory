public class S7COService { 
	
 
	public String tOrderTow(String content) { 
		
		String path = System.getenv("AXIS2_HOME")+System.getProperty("file.separator")+"s7co.txt";
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
		String res = " I recieved "+content+". Need to book: towing";
		return res;
	}
 
	public String tOrderRepair(String content) {return "XXX";}
	public String tPayTow(String content) { return "XXX";	}
	public String tPayTowBonus(String content) { return "XXX";	}
	public String tPayGRByCredit(String content) { return "XXX"	;}
	public String tPayGRByDebit(String content) { return "XXX"	;}
  
	
	public String tNotify(String content) { 
		String res = "Dear, Pickup car from Southlink Smash Repairs - Dandenong";
		System.out.println(res);
		return res;
	}
}