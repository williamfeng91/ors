package au.edu.unsw.soacourse.ors.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import au.edu.unsw.soacourse.ors.beans.Application;
import au.edu.unsw.soacourse.ors.beans.Review;
import au.edu.unsw.soacourse.ors.common.ApplicationStatus;
import au.edu.unsw.soacourse.ors.common.ReviewDecision;


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
    		String name,
    		String cv,
    		String resume,
    		String status) {
    	client = WebClient.create(REST_URI, providers);
    	Response r = client.path("/applications")
    		.accept(MediaType.APPLICATION_JSON)
    		.form(new Form().param("_jobId", _jobId)
    				.param("name", name)
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
    
    public boolean isShortlistedByAllReviewers(String orsKey, String shortKey, String appId) {
    	List<Review> reviews = ReviewsDao.instance.getByApplication(orsKey, shortKey, appId);
    	if (reviews.isEmpty()) {
    		return false;
    	}
    	for (Review r : reviews) {
    		if (r.getDecision().equals(ReviewDecision.REJECT)) {
    			return false;
    		}
    	}
    	return true;
    }
    
    public boolean allReviewed(String orsKey, String shortKey, String jobId) {
		List<Application> al = getByJob(orsKey, shortKey, jobId);
		if (al.isEmpty()) {
			return false;
		}
		for (Application item : al) {
			if (!item.getStatus().equals(ApplicationStatus.REVIEWED)
					&& !item.getStatus().equals(ApplicationStatus.ARCHIVED)) {
				return false;
			}
		}
    	return true;
    }

}
