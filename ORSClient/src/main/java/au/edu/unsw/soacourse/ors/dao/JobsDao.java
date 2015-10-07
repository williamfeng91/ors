package au.edu.unsw.soacourse.ors.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import au.edu.unsw.soacourse.ors.beans.Job;
import au.edu.unsw.soacourse.ors.common.RecruitmentStatus;


public enum JobsDao {
    instance;
    
    private static final String REST_URI = "http://localhost:8080/ORSRestAPI";
    private static final String ORSKEY = "ORSKey";
    private static final String SHORTKEY = "ShortKey";
    private WebClient client;
    private List<Object> providers;

    private JobsDao() {
    	providers = new ArrayList<Object>();
        providers.add( new JacksonJaxbJsonProvider() );
    }

    public Job create(
    		String orsKey,
    		String shortKey,
    		String closingDate,
    		String salary,
    		String positionType,
    		String location,
    		String description,
    		String status,
            String assignedTeam) {
    	client = WebClient.create(REST_URI, providers);
    	Response r = client.path("/jobs")
    		.accept(MediaType.APPLICATION_JSON)
    		.header(ORSKEY, orsKey)
    		.header(SHORTKEY, shortKey)
    		.form(new Form().param("closingDate", closingDate)
    				.param("salary", salary)
    				.param("positionType", positionType)
    				.param("location", location)
    				.param("description", description)
    				.param("status", status)
    				.param("assignedTeam", assignedTeam));
    	Job j = r.readEntity(Job.class);
    	return j;
    }

    public Job getById(String id) {
        client = WebClient.create(REST_URI, providers);
    	client.path("/jobs/" + id).accept(MediaType.APPLICATION_JSON);
    	Job j = client.get(Job.class);
    	return j;
    }

    public List<Job> getAll(String orsKey, String shortKey) {
        client = WebClient.create(REST_URI, providers);
    	client.path("/jobs")
    		.accept(MediaType.APPLICATION_JSON)
    		.header(ORSKEY, orsKey)
    		.header(SHORTKEY, shortKey);
		List<Job> list = (List<Job>) client.getCollection(Job.class);
		return list;
    }
    
    // For external users to see all open jobs
    public List<Job> getOpenJobs() {
    	return search(null, null, null, null, null, null, null, null, null, RecruitmentStatus.CREATED.toString(), null);
    }
    
    // For reviewers to see jobs assigned to them
    public List<Job> getAssignedJobs(String orsKey, String shortKey, String team) {
    	return search(orsKey, shortKey, null, null, null, null, null, null, null, null, team);
    }

    public List<Job> search(
    		String orsKey,
    		String shortKey,
			String closingDateFrom,
			String closingDateTo,
			String salaryFrom,
			String salaryTo,
			String positionType,
			String location,
			String description,
			String status,
			String assignedTeam) {
    	client = WebClient.create(REST_URI, providers);
    	client.path("/jobs")
    		.accept(MediaType.APPLICATION_JSON)
    		.header(ORSKEY, orsKey)
    		.header(SHORTKEY, shortKey);
    	if (closingDateFrom != null && !closingDateFrom.isEmpty()) {
    		client.query("closingDateFrom", closingDateFrom);
    	}
    	if (closingDateTo != null && !closingDateTo.isEmpty()) {
    		client.query("closingDateTo", closingDateTo);
    	}
    	if (salaryFrom != null && !salaryFrom.isEmpty()) {
    		client.query("salaryFrom", salaryFrom);
    	}
    	if (salaryTo != null && !salaryTo.isEmpty()) {
    		client.query("salaryTo", salaryTo);
    	}
    	if (positionType != null && !positionType.isEmpty()) {
    		client.query("positionType", positionType);
    	}
    	if (location != null && !location.isEmpty()) {
    		client.query("location", location);
    	}
    	if (description != null && !description.isEmpty()) {
    		client.query("description", description);
    	}
    	if (status != null && !status.isEmpty()) {
    		client.query("status", status);
    	}
    	if (assignedTeam != null && !assignedTeam.isEmpty()) {
    		client.query("assignedTeam", assignedTeam);
    	}
		List<Job> list = (List<Job>) client.getCollection(Job.class);
		return list;
    }

    public void update(String orsKey, String shortKey, Job updatedJob) {
    	client = WebClient.create(REST_URI, providers);
    	Response r = client.path("/jobs/" + updatedJob.get_jobId())
    		.accept(MediaType.APPLICATION_JSON)
    		.header(ORSKEY, orsKey)
    		.header(SHORTKEY, shortKey)
    		.header("Content-Type", MediaType.APPLICATION_JSON)
    		.put(updatedJob);
    }

    public void delete(String orsKey, String shortKey, String id) {
    	client = WebClient.create(REST_URI, providers);
    	client.path("/jobs/" + id)
			.header(ORSKEY, orsKey)
			.header(SHORTKEY, shortKey)
			.delete();
    }

}
