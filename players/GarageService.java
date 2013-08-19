import org.apache.log4j.Logger;

public class GarageService {
	private static Logger log = Logger.getLogger(GarageService.class.getName());

	public String placeRepairOrder(String content) {
		log.info("placeRepairOrder in GarageService received >>>>>>>>> : " + content );
		return "Werribee";
	}

	public String doRepair(String content) {
		log.info("doRepair in GarageService received >>>>>>>>> : " + content );
		return "OrderRepairResponse";
	}

	public void payRepair(String content) {
		log.info("payRepair in GarageService received >>>>>>>>> : " + content );
		log.info("GR paid for repair service");
	}
	
//	public void setInsuranceFacts(Facts facts)
//	{
//		log.debug("*********************************************");
//		log.debug("facts received by CASEOFFICER back end service: " + facts);
//		log.debug("*********************************************");
//	}
//	
//	public Facts getInsuranceFacts()
//	{
//		log.debug("*********************************************");
//		log.debug("facts sent by CASEOFFICER back end service ");
//		log.debug("*********************************************");
//		return new Facts();
//	}

}
