package au.edu.swin.ict.road.composite.message.containers;

import au.edu.swin.ict.road.composite.message.MessageWrapper;

/**
 * This interface has to be implemented by any class wishing to listen to any of
 * the queues in the Role.
 * 
 * @author Saichander Kukunoor (saichanderreddy@gmail.com)
 */
public interface QueueListener {
    /**
     * Method which is invoked when a message is placed in the queues of a role
     * 
     * @param message
     */
    public void messageReceived(MessageWrapper message);
}
