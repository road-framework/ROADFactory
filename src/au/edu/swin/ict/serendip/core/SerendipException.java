package au.edu.swin.ict.serendip.core;

public class SerendipException extends Exception {
    public SerendipException(String eprStr) {
	super("Serendip Exception: " + eprStr);
    }
}
