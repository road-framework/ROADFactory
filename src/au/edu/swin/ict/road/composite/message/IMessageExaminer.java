package au.edu.swin.ict.road.composite.message;

import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.message.exceptions.MessageException;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface to instantiates many concrete implementation of message checkers.
 * These message checkers acts as message parsers as well as content extractors
 * for the messages passed through a role. It will only be used in case of a
 * message signature conflict, and is based on multiple preferences found inside
 * the message. <br />
 * When implementing this interface for a new supported message type, the class
 * MessageTypeChecker must also be updated to include the new message type.
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
public interface IMessageExaminer {

    /**
     * Function that checks the type of the message, and parse it accordingly to
     * the currently implemented solutions. This is the only method that needs
     * to be implemented whenever there is a new supported message type.
     * 
     * @return the message checker containing the message type
     */
    public IMessageExaminer getMessageExaminer();

    /**
     * Procedure to set the destination contract based on the destination role
     * provided by the routing rules. If the destination role is not specified
     * then it relieves the task to the workload allocator. The workload
     * allocator will check on whose role has the least queue size. <br/>
     * This method must be implemented in the specific message checker and not
     * in the MessageTypeChecker class.
     * 
     * @param roleId
     *            the destination role id for a contract.
     */
    public void evaluateMessage() throws MessageException;

    /**
     * Procedure to add a query name to the checker. A query name is a specific
     * keywords that is being searched for in the outgoing message of a role.
     * These queries would help to route the message to the appropriate roles.
     * Note that the message would still be blocked in the term level if it is
     * not supported by the contract. <br/>
     * This method is to be implemented in the specific message checker.
     * 
     * @param queryName
     *            the query names to be added
     */
    public void addQueryName(String queryName);

    /**
     * Function to retrieve the query names that has been added to be searched.
     * These query names are the ones that is being added by the developer in
     * the routing rules. These query names are the ones that are being looked
     * in the message. <br/>
     * This method is to be implemented in the specific message checker.
     * 
     * @return a list of the query names to be searched
     */
    public List<String> getAddedQueryNames();

    /**
     * Function to retrieve the processed query names. These query names are
     * different from the initial query names. These query names are actually
     * the names of the queries that are found in the message. <br/>
     * This method is to be implemented in the specific message checker.
     * 
     * @return a set of query names in the message
     */
    public Set<String> getQueryResult();

    /**
     * Function to retrieve the processed query names, along with the contained
     * values in the query. This is represented in a map. <br/>
     * This method is to be implemented in the specific message checker.
     * 
     * @return a map containing the query names and values
     */
    public Map<String, String> getQueryValues();

    /**
     * Function to retrieve the content type of the message. <br/>
     * This method is to be implemented in the specific message checker.
     * 
     * @return the content type of the message
     */
    public String getContentType();

    /**
     * Procedure to set the content type of a message. This then would be
     * checked against the supported message content type. <br/>
     * This method is to be implemented in the specific message checker.
     * 
     * @param contentType
     *            the content type of the message
     */
    public void setContentType(String contentType);

    /**
     * Function to retrieve the destination role id. <br/>
     * This function is implemented in the MessageTypeChecker class.
     * 
     * @return the destination role id
     */
    public String getDestinationRole();

    /**
     * Procedure to set the destination role based on the role id defined in the
     * composite XML file. It would retrieve the contract list and iterate
     * through it to check the matching destination role. The message may still
     * be blocked by the term rules. <br/>
     * This function is implemented in the MessageTypeChecker class.
     * 
     * @param roleId
     *            the destination role id
     */
    public void setDestinationRole(String roleId);

    /**
     * Function to retrieve the destination contract. <br/>
     * This function is implemented in the MessageTypeChecker class.
     * 
     * @return the destination contract
     */
    public Contract getDestinationContract();

    /**
     * Procedure to set the destination contract based on the contract id
     * defined in the XML file. If the contract id does not match the contract
     * between two roles, it may still be blocked by the term rules. <br/>
     * This function is implemented in the MessageTypeChecker class.
     * 
     * @param contractId
     *            the destination contract id
     */
    public void setDestinationContract(String contractId);
}
