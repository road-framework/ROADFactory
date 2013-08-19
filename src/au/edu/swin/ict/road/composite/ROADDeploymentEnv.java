package au.edu.swin.ict.road.composite;

/**
 * Use to query the deployment environment properties
 * 
 * @author Malinda
 * 
 */
public interface ROADDeploymentEnv {
    public static final String RDE_ALL_COMPOSITES = "ALL_COMPOSITES";
    public static final String RDE_HOST_URL = "HOST_URL";

    public Object getDepEnvProperty(String key);

    public boolean isCompositeDeployed(String compositeName);
}
