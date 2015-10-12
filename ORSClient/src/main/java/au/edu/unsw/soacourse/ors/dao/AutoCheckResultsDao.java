package au.edu.unsw.soacourse.ors.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import au.edu.unsw.soacourse.ors.beans.AutoCheckResult;


public enum AutoCheckResultsDao {
    instance;
    
    private static final String REST_URI = "http://localhost:8080/ORSRestAPI";
    private static final String ORSKEY = "ORSKey";
    private static final String SHORTKEY = "ShortKey";
    private WebClient client;
    private List<Object> providers;

    private AutoCheckResultsDao() {
    	providers = new ArrayList<Object>();
        providers.add( new JacksonJaxbJsonProvider() );
    }

    public void create(String orsKey, String shortKey, AutoCheckResult newAutoCheckResult) {
    	client = WebClient.create(REST_URI, providers);
    	client.path("/autoCheckResults")
    		.accept(MediaType.APPLICATION_JSON)
    		.header("Content-Type", MediaType.APPLICATION_JSON)
    		.header(ORSKEY, orsKey)
    		.header(SHORTKEY, shortKey)
    		.put(newAutoCheckResult);
    }

    public AutoCheckResult getById(String orsKey, String shortKey, String id) {
        client = WebClient.create(REST_URI, providers);
    	client.path("/autoCheckResults/" + id)
    		.accept(MediaType.APPLICATION_JSON)
    		.header(ORSKEY, orsKey)
    		.header(SHORTKEY, shortKey);
    	AutoCheckResult a = client.get(AutoCheckResult.class);
    	return a;
    }

    public AutoCheckResult getByApplication(String orsKey, String shortKey, String appId) {
        client = WebClient.create(REST_URI, providers);
    	client.path("/applications/" + appId + "/autoCheckResults")
    		.accept(MediaType.APPLICATION_JSON)
    		.header(ORSKEY, orsKey)
    		.header(SHORTKEY, shortKey);
    	try {
    		AutoCheckResult result = (AutoCheckResult) client.get(AutoCheckResult.class);
    		return result;
    	} catch (Exception e) {
    		return null;
    	}
    }

    public List<AutoCheckResult> getAll(String orsKey, String shortKey) {
        client = WebClient.create(REST_URI, providers);
    	client.path("/autoCheckResults")
    		.accept(MediaType.APPLICATION_JSON)
    		.header(ORSKEY, orsKey)
    		.header(SHORTKEY, shortKey);
		List<AutoCheckResult> list = (List<AutoCheckResult>) client.getCollection(AutoCheckResult.class);
		return list;
    }

}
