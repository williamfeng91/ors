package au.edu.unsw.soacourse.ors.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import au.edu.unsw.soacourse.ors.beans.Review;


public enum ReviewsDao {
    instance;
    
    private static final String REST_URI = "http://localhost:8080/ORSRestAPI";
    private static final String ORSKEY = "ORSKey";
    private static final String SHORTKEY = "ShortKey";
    private WebClient client;
    private List<Object> providers;

    private ReviewsDao() {
    	providers = new ArrayList<Object>();
        providers.add( new JacksonJaxbJsonProvider() );
    }
    
    public Review create(
    		String orsKey,
    		String shortKey,
    		String _appId,
    		String _uId,
    		String comments,
    		String decision) {
    	client = WebClient.create(REST_URI, providers);
    	Response r = client.path("/reviews")
    		.accept(MediaType.APPLICATION_JSON)
    		.header(ORSKEY, orsKey)
    		.header(SHORTKEY, shortKey)
    		.form(new Form().param("_appId", _appId)
    				.param("_uId", _uId)
    				.param("comments", comments)
    				.param("decision", decision));
    	Review review = r.readEntity(Review.class);
    	return review;
    }
    
    public Review getById(String orsKey, String shortKey, String id) {
        client = WebClient.create(REST_URI, providers);
    	client.path("/reviews/" + id)
    		.accept(MediaType.APPLICATION_JSON)
    		.header(ORSKEY, orsKey)
    		.header(SHORTKEY, shortKey);
    	Review r = client.get(Review.class);
    	return r;
    }
    
    public List<Review> getByApplication(String orsKey, String shortKey, String appId) {
        client = WebClient.create(REST_URI, providers);
    	client.path("/applications/" + appId + "/reviews")
    		.accept(MediaType.APPLICATION_JSON)
    		.header(ORSKEY, orsKey)
    		.header(SHORTKEY, shortKey);
    	List<Review> list = (List<Review>) client.getCollection(Review.class);
		return list;
    }
    
    public List<Review> getByReviewer(String orsKey, String shortKey, String uId) {
        client = WebClient.create(REST_URI, providers);
    	client.path("/users/" + uId + "/reviews")
    		.accept(MediaType.APPLICATION_JSON)
    		.header(ORSKEY, orsKey)
    		.header(SHORTKEY, shortKey);
    	List<Review> list = (List<Review>) client.getCollection(Review.class);
		return list;
    }
    
    public List<Review> getAll(String orsKey, String shortKey) {
        client = WebClient.create(REST_URI, providers);
    	client.path("/reviews")
    		.accept(MediaType.APPLICATION_JSON)
    		.header(ORSKEY, orsKey)
    		.header(SHORTKEY, shortKey);
    	List<Review> list = (List<Review>) client.getCollection(Review.class);
		return list;
    }
    
    public boolean hasBeenReviewedBy(String orsKey, String shortKey, String appId, String uId) {
    	try {
	    	List<Review> reviews = getByApplication(orsKey, shortKey, appId);
	    	for (Review r : reviews) {
	    		if (r.get_uId().equals(uId)) {
	    			return true;
	    		}
	    	}
	    	return false;
    	} catch (Exception e) {
    		return false;
    	}
    }

}
