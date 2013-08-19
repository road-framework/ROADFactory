import org.apache.log4j.Logger;


public class TowCarService {
	private static Logger log = Logger.getLogger(TowCarService.class.getName());

	public TowReturn tow(String pickupLocation, String garageLocation) {
		log.info("Tow in TowCarService received >>>>>>>>> : " + pickupLocation
				+ " :::::: " + garageLocation);
		TowReturn result = new TowReturn();
		result.setOrderTowResponse("OrderTowResponse");
		result.setSendGRLocationResponse("SendGRLocationResponse");
		return result;
	}

	public void payTow(String content) {
		log.info("payTow in TowCarService received >>>>>>>>> : " + content);
		log.debug("TC paid for towing service");
	}

	public void setInsuranceFacts(PFacts facts) {
		log.info("TowCarService no of facts received = " + facts.getFact().length);
		for (PFact fact : facts.getFact()) {
			log.info(fact.getName() + " : " + fact.getSource()
					+ " : " + fact.getIdentifier().getIdentifierKey() + " : "
					+ fact.getIdentifier().getIdentifierValue());
			PFactAttribute[] attributes = fact.getAttributes().getAttribute();
			for (PFactAttribute attribute : attributes) {
				log.info("Key : Value >>>> " + attribute.getAttributeKey()
						+ " : " + attribute.getAttributeValue());
			}
		}
	}

	public PFacts getInsuranceFacts() {
		log.info("*********************************************");
		log.info("facts sent by CASEOFFICER back end service ");
		log.info("*********************************************");
		return new PFacts();
	}
}
