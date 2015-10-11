package au.edu.unsw.soacourse.ors.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import au.edu.unsw.soacourse.ors.beans.Application;
import au.edu.unsw.soacourse.ors.beans.AutoCheckResult;
import au.edu.unsw.soacourse.ors.beans.Job;


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
    	Response r = client.path("/autoCheckResults")
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
    	AutoCheckResult result = (AutoCheckResult) client.get(AutoCheckResult.class);
		return result;
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
    
    public boolean allDone(String orsKey, String shortKey, String jobId) {
    	List<Application> applications = ApplicationsDao.instance.getByJob(orsKey, shortKey, jobId);
    	for (Application a : applications) {
    		try {
    			getByApplication(orsKey, shortKey, a.get_appId());
    		} catch (Exception e) {
    			return false;
    		}
    	}
    	return true;
    }

}
