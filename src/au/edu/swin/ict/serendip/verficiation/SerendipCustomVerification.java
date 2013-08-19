package au.edu.swin.ict.serendip.verficiation;

import au.edu.swin.ict.serendip.core.ModelProviderFactory;

public interface SerendipCustomVerification {
    public abstract boolean verify(ModelProviderFactory modelProviderFactory)
	    throws SerendipVerificationException;
}
