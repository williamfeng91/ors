package au.edu.unsw.soacourse.ors.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import au.edu.unsw.soacourse.ors.beans.Application;


public enum ApplicationsDao {
    instance;
    
    private static final String REST_URI = "http://localhost:8080/ORSRestAPI";
    private static final String ORSKEY = "ORSKey";
    private static final String SHORTKEY = "ShortKey";
    private WebClient client;
    private List<Object> providers;

    private ApplicationsDao() {
    	providers = new ArrayList<Object>();
        providers.add( new JacksonJaxbJsonProvider() );
    }

    public Application create(
    		String _jobId,
    		String licenseNo,
    		String fullName,
    		String postcode,
    		String cv,
    		String resume,
    		String status) {
    	client = WebClient.create(REST_URI, providers);
    	Response r = client.path("/applications")
    		.accept(MediaType.APPLICATION_JSON)
    		.form(new Form().param("_jobId", _jobId)
    				.param("licenseNo", licenseNo)
    				.param("fullName", fullName)
    				.param("postcode", postcode)
    				.param("cv", cv)
    				.param("resume", resume)
    				.param("status", status));
    	Application a = r.readEntity(Application.class);
    	return a;
    }

    public Application getById(String id) {
        client = WebClient.create(REST_URI, providers);
    	client.path("/applications/" + id).accept(MediaType.APPLICATION_JSON);
    	Application a = client.get(Application.class);
    	return a;
    }

    public List<Application> getByJob(String orsKey, String shortKey, String jobId) {
        client = WebClient.create(REST_URI, providers);
    	client.path("/jobs/" + jobId + "/applications")
    		.accept(MediaType.APPLICATION_JSON)
    		.header(ORSKEY, orsKey)
    		.header(SHORTKEY, shortKey);
    	List<Application> list = (List<Application>) client.getCollection(Application.class);
		return list;
    }

    public List<Application> getAll(String orsKey, String shortKey) {
        client = WebClient.create(REST_URI, providers);
    	client.path("/applications")
    		.accept(MediaType.APPLICATION_JSON)
    		.header(ORSKEY, orsKey)
    		.header(SHORTKEY, shortKey);
		List<Application> list = (List<Application>) client.getCollection(Application.class);
		return list;
    }

    public void update(Application updatedApplication) {
    	client = WebClient.create(REST_URI, providers);
    	Response r = client.path("/applications/" + updatedApplication.get_appId())
    		.accept(MediaType.APPLICATION_JSON)
    		.header("Content-Type", MediaType.APPLICATION_JSON)
    		.put(updatedApplication);
    }

    public void delete(String id) {
    	client = WebClient.create(REST_URI, providers);
    	client.path("/applications/" + id)
			.delete();
    }

}
