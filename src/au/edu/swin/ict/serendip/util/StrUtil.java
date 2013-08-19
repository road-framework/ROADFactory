package au.edu.swin.ict.serendip.util;

import java.util.StringTokenizer;

public class StrUtil {
    public static String[] getEventsFromEventPattern(String eventPattern) {
	// [RepairOK] AND [GRPaid] OR [TCPaid]
	String str = eventPattern;
	int i = 0;

	str = str.replaceAll("AND", "").trim();
	str = str.replaceAll("OR", "").trim();
	str = str.replaceAll("XOR", "").trim();
	str = str.replaceAll(" ", "").trim();

	StringTokenizer st = new StringTokenizer(str, "[]");
	String[] strArray = new String[st.countTokens()];
	while (st.hasMoreTokens()) {
	    strArray[i++] = st.nextToken();
	}
	return strArray;
    }

    public static String ArrayToStr(String[] strArr, String seperatedBy) {
	String str = "";
	for (int i = 0; i < strArr.length; i++) {
	    str += strArr[i] + seperatedBy;
	}
	return str;
    }

    public static String removeQuotes(String str) {
	if (null == str) {
	    return null;
	}
	if (str.startsWith("\"")) {
	    str = str.substring(1, str.length());
	}
	if (str.endsWith("\"")) {
	    str = str.substring(0, str.length() - 1);
	}
	return str;
    }
}
