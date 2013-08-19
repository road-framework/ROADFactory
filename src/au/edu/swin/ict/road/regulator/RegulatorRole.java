/**
 * 
 */
package au.edu.swin.ict.road.regulator;

import java.util.List;

/**
 * @author Aditya
 * 
 */
public class RegulatorRole implements IContextProvider {

    /*
     * (non-Javadoc)
     * 
     * @see au.edu.swin.ict.road.regulator.IContextProvider#getExternalFacts()
     */
    @Override
    public List<FactObject> getExternalFacts() {
	// TODO Auto-generated method stub
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * au.edu.swin.ict.road.regulator.IContextProvider#notifyFactSource(java
     * .lang.String)
     */
    @Override
    public void notifyFactSource(String factXML) {
	// TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see au.edu.swin.ict.road.regulator.IContextProvider#pollFacts()
     */
    @Override
    public String pollFacts() {
	// TODO Auto-generated method stub
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * au.edu.swin.ict.road.regulator.IContextProvider#putFacts(java.lang.String
     * )
     */
    @Override
    public void putFacts(String factXML) {
	// TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * au.edu.swin.ict.road.regulator.IContextProvider#retrieveFactUpdates()
     */
    @Override
    public String retrieveFactUpdates() {
	// TODO Auto-generated method stub
	return null;
    }

}
