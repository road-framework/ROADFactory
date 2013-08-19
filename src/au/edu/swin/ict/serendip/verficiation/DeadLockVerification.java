package au.edu.swin.ict.serendip.verficiation;

import au.edu.swin.ict.serendip.core.ModelProviderFactory;

/**
 * Make sure that the processes are deadlock free. Task1 triggering event e1
 * waiting for e2 which is triggerd by task2 waiting for e1
 * 
 * @author Malinda Kapuruge
 * @deprecated
 * @see DefaultVerification
 */
public class DeadLockVerification implements SerendipCustomVerification {

    @Override
    public boolean verify(ModelProviderFactory modelProviderFactory)
	    throws SerendipVerificationException {
	// TODO Auto-generated method stub
	return true;
    }

}
