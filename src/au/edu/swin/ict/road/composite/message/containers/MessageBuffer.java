package au.edu.swin.ict.road.composite.message.containers;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.Role;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.message.exceptions.MessageException;

/**
 * The class to keep messages in a buffer. Messages can be picked irrespective
 * of the order they come.
 * 
 * @author Malinda
 * 
 */
public class MessageBuffer {
    private static Logger log = Logger.getLogger(MessageBuffer.class.getName());
    // private Hashtable<String, MessageWrapper> ht = new Hashtable<String,
    // MessageWrapper>();
    private Vector<MessageWrapper> collection = new Vector<MessageWrapper>();
    private BufferType type = BufferType.NONE;
    private Role role = null;

    public enum BufferType {
	NONE, ROUTER, PENDINGOUT
    }

    public MessageBuffer(Role role, BufferType bufferType) {
	this.role = role;
	this.type = bufferType;
    }

    /**
     * Return the message for a given msgId and the correlation id (process
     * instance id)
     * 
     * @param msgId
     * @param correlationId
     * @return
     * @throws MessageException
     */
    public MessageWrapper getMessage(String msgId, String correlationId)
	    throws MessageException {
	for (MessageWrapper mw : collection) {
	    if (null == correlationId) {// looking for a message that is not
					// associated with a correlation id
					// (process id)
		if (mw.getMessageId().equals(msgId)) {
		    return mw;
		}
	    } else {// need correlation id too
		if (mw.getMessageId().equals(msgId)
			&& mw.getCorrelationId().equals(correlationId)) {
		    return mw;
		}
	    }
	}
	return null;
    }

    public void dropMessage(MessageWrapper mw) throws MessageException {
	log.info("Message " + mw.getMessageId() + "," + mw.getCorrelationId()
		+ " dropped to " + this.role.getId() + "." + this.getType());
	this.collection.add(mw);
    }

    public void removeMessage(String msgId, String correlationId)
	    throws MessageException {

	MessageWrapper mw = this.getMessage(msgId, correlationId);
	if (null != mw) {
	    this.collection.remove(mw);
	} else {
	    log.error("Message " + msgId + "," + correlationId
		    + " not found in " + this.role.getId() + "."
		    + this.getType() + ". Cannot remove");
	}
    }

    public Enumeration<MessageWrapper> getAllMessages() {
	// return ht.elements();
	return this.collection.elements();
    }

    /**
     * This method combine the msgId and the correlation id to generate the key
     * 
     * @param msgId
     * @param correlationId
     * @return
     * @throws MessageException
     */
    private String getKey(String msgId, String correlationId)
	    throws MessageException {
	if (msgId == null) {
	    throw new MessageException("Message id cannot be null");
	}
	if (null == correlationId) {
	    return msgId;
	} else {
	    return msgId + correlationId;
	}
    }

    public BufferType getType() {
	return type;
    }

    public void setType(BufferType type) {
	this.type = type;
    }

}
