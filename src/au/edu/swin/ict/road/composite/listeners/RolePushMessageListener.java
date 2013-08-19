package au.edu.swin.ict.road.composite.listeners;

import au.edu.swin.ict.road.composite.IRole;

/**
 * Any message pusher need to implement this interface. Then a message pusher
 * need to be registered. role.registerNewPushListener(this.messagePusher);
 * 
 * @author Malinda
 * 
 */
public interface RolePushMessageListener {

    public void pushMessageRecieved(IRole role);

}
