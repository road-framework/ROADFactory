package au.edu.swin.ict.serendip.verficiation;

import au.edu.swin.ict.serendip.core.SerendipException;

public class SerendipVerificationException extends SerendipException {
    public SerendipVerificationException(String eprStr) {
	super("Verification failed: " + eprStr);
    }
}
