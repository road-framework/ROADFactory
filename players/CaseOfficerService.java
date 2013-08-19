import org.apache.log4j.Logger;

public class CaseOfficerService {
	private static Logger log = Logger.getLogger(CaseOfficerService.class
			.getName());

	public AnalyzeReturn analyze(String memId, String complainDetails) {
		log.info("analyze in CaseOfficerService received >>>>>>>>> : " + memId
				+ " :::: " + complainDetails);
		AnalyzeReturn order = new AnalyzeReturn();
		order.setOrderTow(complainDetails);
		order.setOrderRepair("OrderRepairRequest");
		return order;
	}

	public String payTC(String content) {
		log.info("payTC in CaseOfficerService received >>>>>>>>> : " + content);
		return "PayTowRequest";
	}

	public String payGR(String content) {
		log.info("payGR in CaseOfficerService received >>>>>>>>> : " + content);
		return "PayRepairRequest";
	}

	public void setInsuranceFacts(PFacts facts) {
		log.info("CaseOfficer service >>>>>>>>>> no of facts received = " + facts.getFact().length);
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
