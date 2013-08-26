ROADfactory 2.0
===========

ROADfactory version 2.0. A part of the ROAD tool chain.  
Developed by the ROAD team, Swinburne University of Technology (http://www.swinburne.edu.au/ict/research/cs3/road/).  
For questions or comments please contact Malinda Kapuruge (mkapuruge at swin dot edu dot au).

Major Improvements:
-----------------------------
* New message mediation architecture. 
* Support for business processes.
* Support for context awareness.

Requirements:
-----------------------------
* Requires Java 6 or greater.
* Requires Axis2 1.6 or greater 
* Requires Tomcat 7.0 or greater 
* Requires ROAD4WS 2.0 (for web service deployment)

Axis2 Installation (for web service deployment):
-----------------------------
Note: **You need to install ROAD4WS first. See road4ws/README for more information.**
* Method1 (Ant): 
	* Open build.xml -> run ant-task 'install'.
* Method2 (Script)
	* Run install-in-axis2.bat
Note: This will install all the sample composites too. 

Verify Installation:
-----------------------------
* Start server by running %TOMCAT_HOME%/bin/startup.bat
* Open location in a browser http://localhost:8080/axis2/services/listServices
* See the deployed service composites, e.g., USDL_RUP

Run samples client:
-----------------------------
Example Scenario1:  
 * Run Sample/Client motorist.MotoristClient.java 
	
Example Scenario2:  
 * Run Sample/Client usdl.SP1Client.java

Monitor runtime activites/processes
-----------------------------
* See the tomcat console  
Alternatively, for a visually monitor the processes running in a composite you may use Serendip Process Monitor.  
* Set property SERENDIP_SHOW_ADMIN_VIEW=TRUE in file %TOMCAT_HOME%/bin/serendip.properties
* Start Tomcat
* Run the Scenario1 again

How to instantiate a Self-Managed Composite (Standalone)
-----------------------------
```
public Composite instantiateRunningSMC(String file) {
		Composite c = null;
		try {
			CompositeDemarshaller dm = new CompositeDemarshaller();
			c = dm.demarshalSMC(file);
			Thread cthread = new Thread(c);
			cthread.start();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		// sleep to give composite time to start
		try {
			Thread.sleep(1000);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return c;
}	
```		
Bug report
-----------------------------
* mkapuruge at swin dot edu dot au
* Make sure to attach all log files ( %TOMCAT_HOME%/logs/*,  %TOMCAT_HOME%/bin/Road.log)  
And provide as much details as possible. 

Achnowledgement
-----------------------------
This project has been researched & developed by Swinburne University of Technology partly with the support of Smart Services CRC. To find out more about Smart Services please visit www.smartservicescrc.com.au.

