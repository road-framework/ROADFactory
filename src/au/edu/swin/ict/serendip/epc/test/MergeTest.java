package au.edu.swin.ict.serendip.epc.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.processmining.analysis.epcmerge.EPCMergeMethod;
import org.processmining.framework.models.epcpack.ConfigurableEPC;

import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.epc.EPMLReader;
import au.edu.swin.ict.serendip.epc.EPMLWriter;
import au.edu.swin.ict.serendip.epc.MergeBehavior;
import au.edu.swin.ict.serendip.epc.PatternToEPC;
import au.edu.swin.ict.serendip.tool.gui.SerendipEPCView;

public class MergeTest {
    private static Logger log = Logger.getLogger(MergeTest.class.getName());

    /**
     * Test how two behaviors are merged
     * 
     * @param args
     * @throws SerendipException
     */
    public static void main(String[] args) throws SerendipException {
	// mergeEPML();

	// mergeTasks();

	// showMergeDiff();

	// newMergeTest();
	// mergeTestWISE();
	mergeTestCase1();
    }

    
    public static void mergeTestCase1() throws SerendipException {
	ConfigurableEPC[] epcs = {
		PatternToEPC.convertToEPC("eAsstReqd  ", " tOrderTow",
			" eTowReqd", "TT", null, null, null),
		PatternToEPC.convertToEPC("eTowDone  ", "tOrderRepair ",
			" eRepairReqd"),
		PatternToEPC.convertToEPC("eTowPay ", " tPayTowBonus",
			"eTowPaid "),
		PatternToEPC.convertToEPC("eTowBonusPay ", " tPayTow",
			"eTowPaid"),
		PatternToEPC.convertToEPC("eRepairCreditPay   ",
			" tPayGRByCredit", " eRepairPaid"),
		PatternToEPC.convertToEPC("eRepairDebitPay   ",
			"tPayGRByDebit ", " eRepairPaid"),
		PatternToEPC.convertToEPC("eRepairReqd ", "tRepair ",
			" eRepairCreditPay ^ eRepairDebitPay"),
		PatternToEPC.convertToEPC(" eTowReqd  ", "tTow ",
			"eTowDone *(eTowPay ^ eTowBonusPay) "),
		PatternToEPC.convertToEPC(" eTowPaid * eRepairPaid  ",
			"tFinalise ", "eProcessComplete") };
	// TASKS
	// ------------
	// eAsstReqd-> tOrderTow->eTowReqd
	// eTowDone->tOrderRepair-> eRepairReqd
	// eTowPay- > tPayTowBonus ->eTowPaid
	// eTowBonusPay-> tPayTow ->eTowPaid
	// eRepairCreditPay->tPayGRByCredit ->eRepairPaid
	// eRepairDebitPay->tPayGRByDebit ->eRepairPaid
	// eRepairReqd->tRepair->eRpairDone * (eRepairCreditPay ^
	// eRepairDebitPay)
	// eTowReqd->tTow->eTowDone *(eTowPay ^ eTowBonusPay)

	ConfigurableEPC result = epcs[0];
	// frameIt(new SerendipEPCView( "epc0" , epcs[0]));
	for (int i = 1; i < epcs.length; i++) {
	    // frameIt(new SerendipEPCView( "epc"+i, epcs[i]));

	    result = MergeBehavior.mergeEPC(result, epcs[i]);
	}

	frameIt(new SerendipEPCView("test", result));
	log.debug("Done");

    }

    public static void mergeTestWISE() throws SerendipException {
	ConfigurableEPC[] epcs = {
		PatternToEPC.convertToEPC("eAsstReqd  ", " tOrderTow",
			" eTowReqd"),
		PatternToEPC.convertToEPC("eTowDone  ", "tOrderRepair ",
			" eRepairReqd"),
		PatternToEPC.convertToEPC("eTowPay ", " tPayTowBonus",
			"eTowPaid "),
		PatternToEPC.convertToEPC("eTowBonusPay ", " tPayTow",
			"eTowPaid"),
		PatternToEPC.convertToEPC("eRepairCreditPay   ",
			" tPayGRByCredit", " eRepairPaid"),
		PatternToEPC.convertToEPC("eRepairDebitPay   ",
			"tPayGRByDebit ", " eRepairPaid"),
		PatternToEPC.convertToEPC("eRepairReqd ", "tRepair ",
			" eRpairDone * (eRepairCreditPay ^ eRepairDebitPay)"),
		PatternToEPC.convertToEPC(" eTowReqd  ", "tTow ",
			"eTowDone *(eTowPay ^ eTowBonusPay) "),
		PatternToEPC.convertToEPC(" eTowPaid * eRepairPaid  ",
			"tFinalise ", "eProcessComplete") };
	// TASKS
	// ------------
	// eAsstReqd-> tOrderTow->eTowReqd
	// eTowDone->tOrderRepair-> eRepairReqd
	// eTowPay- > tPayTowBonus ->eTowPaid
	// eTowBonusPay-> tPayTow ->eTowPaid
	// eRepairCreditPay->tPayGRByCredit ->eRepairPaid
	// eRepairDebitPay->tPayGRByDebit ->eRepairPaid
	// eRepairReqd->tRepair->eRpairDone * (eRepairCreditPay ^
	// eRepairDebitPay)
	// eTowReqd->tTow->eTowDone *(eTowPay ^ eTowBonusPay)

	ConfigurableEPC result = epcs[0];
	// frameIt(new SerendipEPCView( "epc0" , epcs[0]));
	for (int i = 1; i < epcs.length; i++) {
	    // frameIt(new SerendipEPCView( "epc"+i, epcs[i]));

	    result = MergeBehavior.mergeEPC(result, epcs[i]);
	}

	frameIt(new SerendipEPCView("test", result));
	log.debug("Done");

    }

    public static void newMergeTest() throws SerendipException {
	// ConfigurableEPC[] epcs = {
	// PatternToEPC.convertToEPC(" e1 * e2 ", "TC.Tow",
	// "(e3  ^ e4) * e5 ")
	// ,PatternToEPC.convertToEPC(" e3 ", "CO.PayTC",
	// "e6")
	// };

	ConfigurableEPC[] epcs = { PatternToEPC.convertToEPC(
		"  eTowRequested * ePickupLocKnown ", "TT.tTow",
		"eCarTowed ^ eTowFailed") };
	ConfigurableEPC result = epcs[0];
	frameIt(new SerendipEPCView("epc0", epcs[0]));
	for (int i = 1; i < epcs.length; i++) {
	    frameIt(new SerendipEPCView("epc" + i, epcs[i]));

	    result = MergeBehavior.mergeEPC(result, epcs[i]);
	}

	frameIt(new SerendipEPCView("test", result));
	log.debug("Done");
    }

    public static void frameIt(SerendipEPCView epcView) {
	JFrame frame = new JFrame();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().add(epcView);
	frame.setBounds(10, 10, 400, 600);
	frame.setVisible(true);
    }

    public static void showMergeDiff() throws SerendipException {
	ConfigurableEPC epc1 = PatternToEPC.convertToEPC("e11 AND e12", "t1",
		"e19 OR e25");
	ConfigurableEPC epc2 = PatternToEPC.convertToEPC("e11 AND e3 AND e19 ",
		"t2", "e24 | e25");
	show(epc1, "epc1");
	show(epc2, "epc2");
	EPCMergeMethod mergeMethod = new EPCMergeMethod(epc1, epc2);
	ConfigurableEPC result1 = mergeMethod.analyse();
	show(result1, "result-intermediate");
	ConfigurableEPC result2 = MergeBehavior.mergeEPC(epc1, epc2);
	show(result2, "result-final");
    }

    public static void show(ConfigurableEPC epc, String title) {
	SerendipEPCView epcView = new SerendipEPCView(title, epc);
	JFrame frame = new JFrame(title);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().add(epcView);
	frame.setBounds(10, 10, 400, 600);
	frame.setVisible(true);
    }

    public static void mergeTasks() throws SerendipException {
	/*
	 * BT_Towing: ComplaintRcvd->CO.SendTowReq->TowReqSent & PickupLocKnown
	 * TowReqSent->GR.SendGRLocation->DestinationKnown PickupLocKnown &
	 * DestinationKnown -> TC.Tow-> CarTowSuccess | CarTowFailed
	 * CarTowSuccess-> GR.TowingAck -> TowingAckedByGR CarTowSuccess &
	 * TowingAckedByGR -> CO.PayTow -> TCPaid
	 */
	ConfigurableEPC[] epcs = {
		// BT_Complain
		PatternToEPC.convertToEPC("[ANY]", "MM.Complain",
			"[ComplaintRcvd]"),
		PatternToEPC.convertToEPC("[ComplaintRcvd]", "CO.SendAck",
			"[ComplaintAcked]"),
		PatternToEPC.convertToEPC("[ComplaintRcvd]", "CO.Analyze",
			"[TowReqd] | [RepairReqd]"),

		// BT_Towing
		PatternToEPC.convertToEPC("[TowReqd]", "CO.SendTowReq",
			"[TowReqSent] * [PickupLocKnown]"),
		PatternToEPC.convertToEPC("[TowReqSent]", "GR.SendGRLocation",
			"[DestinationKnown]"),
		PatternToEPC.convertToEPC(
			"[PickupLocKnown] * [DestinationKnown]", "TC.Tow",
			"[CarTowSuccess] | [CarTowFailed]"),
		PatternToEPC.convertToEPC("[CarTowSuccess]", "GR.TowingAck",
			"[TowingAckedByGR]"),
		PatternToEPC.convertToEPC(
			"[CarTowSuccess] * [TowingAckedByGR]", "CO.PayTow",
			"[TCPaid]"),

		PatternToEPC.convertToEPC("[CarTowSuccess]", "GR.PayIncentive",
			"[IncentivePaid]"),

		// BT_Repair
		PatternToEPC.convertToEPC("[RepairReqd]", "CO.SendGRReq",
			"[GRReqSent]"),
		PatternToEPC.convertToEPC("[GRReqSent] * [CarTowSuccess]",
			"GR.ReqAdvPay", "[AdvPayReqSent]"),
		PatternToEPC.convertToEPC("[CarTowSuccess] * [AdvPayReqSent]",
			"CO.PayAdvToGR", "[AdvToGRPaid]"),
		PatternToEPC.convertToEPC("[CarTowSuccess] * [AdvToGRPaid]",
			"GR.Repair", "[CarRepairSuccess] * [CarRepairFailed]"),
		PatternToEPC.convertToEPC("[CarRepairSuccess]", "MM.Inspect",
			"[CarRepairOKConfirmed] | [CarRepairFailureNoified]"),
		PatternToEPC.convertToEPC(
			"[CarRepairSuccess] * [CarRepairOKConfirmed] ",
			"CO.PayGR", "[GRPaid]"),

		// BT_ProvideTaxi
		PatternToEPC.convertToEPC("[ANY]", "MM.CallTaxi",
			"[TaxiCallRcvd]"),
		PatternToEPC.convertToEPC("[ComplaintRcvd] * [TaxiCallRcvd]",
			"CO.SendTaxiReq", "[TaxiRequested]"),
		PatternToEPC.convertToEPC("[TaxiRequested]",
			"TX.AckTaxiReqRcvd", "[TaxiReqAcked]"),
		PatternToEPC.convertToEPC("[TaxiReqAcked]",
			"TX.ProvideTaxiService", "[TaxiSvcProvided]"),
		PatternToEPC.convertToEPC("[TaxiSvcProvided]",
			"TX.ProvideTaxiService", "[PayTaxi]"),

		// BT_ProvideHotel
		PatternToEPC.convertToEPC("[ANY]", "MM.RequestAccommodation",
			"[HotelReqd]"),
		PatternToEPC.convertToEPC("[ComplaintRcvd] * [HotelReqd]",
			"CO.BookHotel", "[HotelReqSent]"),
		PatternToEPC.convertToEPC("[HotelReqSent]",
			"HT.SendBookingConfirmation", "[HotelBooked]"),
		PatternToEPC.convertToEPC("[HotelBooked]",
			"CO.NotifyMemberOfHotel", "[MemberNotifiedAboutHotel]"),

	};

	ConfigurableEPC result = epcs[0];
	for (int i = 1; i < epcs.length; i++) {
	    result = MergeBehavior.mergeEPC(result, epcs[i]);
	}
	log.debug("Done");
	SerendipEPCView epcView = new SerendipEPCView("test", result);
	JFrame frame = new JFrame();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().add(epcView);
	frame.setBounds(10, 10, 400, 600);
	frame.setVisible(true);
    }

    public static void mergeEPML() {
	// TODO Auto-generated method stub

	ConfigurableEPC epc1 = null, epc2 = null, epc3 = null, epc4 = null, epc5 = null, mergedEpc = null;

	File file1 = new File("sample/composites/RoSAS2/CO-GR_B1.epml");
	File file2 = new File("sample/composites/RoSAS2/CO-MM_B1.epml");
	File file3 = new File("sample/composites/RoSAS2/CO-TC_B1.epml");
	File file4 = new File("sample/composites/RoSAS2/GR-TC_B1.epml");
	File file5 = new File("sample/composites/RoSAS2/MM-GR_B1.epml");

	try {
	    epc1 = EPMLReader.getEPCFromFile(file1);
	    epc2 = EPMLReader.getEPCFromFile(file2);
	    epc3 = EPMLReader.getEPCFromFile(file3);
	    epc4 = EPMLReader.getEPCFromFile(file4);
	    epc5 = EPMLReader.getEPCFromFile(file5);

	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	// EPMLBehavior.mergeEPC(epc1, epc2);
	// EPCMergeMethod mergeMethod = new EPCMergeMethod(epc1, epc2);
	// mergedEpc = mergeMethod.analyse();

	MergeBehavior merge = new MergeBehavior(new ConfigurableEPC[] { epc1,
		epc2, epc3, epc4, epc5 });
	mergedEpc = merge.getMergedBehaviorAsEPC();

	EPMLWriter w = new EPMLWriter(mergedEpc, true);
	try {
	    w.writeToFile("sample/composites/RoSAS2/Merged.epml");
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
