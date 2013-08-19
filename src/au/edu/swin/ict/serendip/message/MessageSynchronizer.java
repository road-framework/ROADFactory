package au.edu.swin.ict.serendip.message;

/**
 * Known subclass SOAPMessageSynchronizer
 * 
 * @author Malinda Kapuruge
 * 
 */
public interface MessageSynchronizer {

    public Message synchronizeMessages(Message[] messages);
}
