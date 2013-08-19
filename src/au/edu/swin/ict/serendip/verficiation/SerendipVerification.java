package au.edu.swin.ict.serendip.verficiation;

import org.apache.log4j.Logger;

import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.ModelProviderFactory;
import au.edu.swin.ict.serendip.core.ProcessInstance;

public interface SerendipVerification {
    static Logger logger = Logger.getLogger(SerendipVerification.class);

    /**
     * Verify the composition as a whole. This should call the
     * <code>verifyProcess()</code> iteratively for all the process definitions
     * 
     * @param modelProviderFactory
     * @return
     * @throws SerendipVerificationException
     */
    public abstract VerificationResult verify(
	    ModelProviderFactory modelProviderFactory)
	    throws SerendipVerificationException;

    /**
     * Verify a given process definition
     * 
     * @param modelProviderFactory
     * @param definitionId
     * @return
     * @throws SerendipVerificationException
     */
    public abstract VerificationResult verifyProcessDef(
	    ModelProviderFactory modelProviderFactory, String definitionId)
	    throws SerendipVerificationException;

    /**
     * Verify a single process instance upon ad-hoc modifications
     * 
     * @param pi
     * @return
     */
    public abstract VerificationResult verifyProcessInstance(
	    ProcessInstance pi, ModelProviderFactory modelProviderFactory)
	    throws SerendipVerificationException;

    /**
     * Might not need to be implemented by all the verifiers. Just return a
     * result with value=true
     * 
     * @param modelProviderFactory
     * @param behaviorId
     * @return
     * @throws SerendipVerificationException
     */
    public abstract VerificationResult verifyBehavior(
	    ModelProviderFactory modelProviderFactory, String behaviorId)
	    throws SerendipVerificationException;
}
