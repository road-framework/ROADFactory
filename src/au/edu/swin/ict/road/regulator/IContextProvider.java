package au.edu.swin.ict.road.regulator;

import java.util.List;

public interface IContextProvider {
    public String pollFacts();

    public void notifyFactSource(String factXML);

    public void putFacts(String factXML);

    public String retrieveFactUpdates();

    public List<FactObject> getExternalFacts();
}
