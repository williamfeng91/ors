package au.edu.unsw.soacourse.ors;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import au.edu.unsw.soacourse.ors.dao.ApplicationsDao;
import au.edu.unsw.soacourse.ors.dao.JobsDao;
import au.edu.unsw.soacourse.ors.dao.UsersDao;
import au.edu.unsw.soacourse.ors.model.*;
import au.edu.unsw.soacourse.ors.security.Security;

@Path("/jobs")
public class JobsResource {
	// Allows to insert contextual objects into the class, 
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	// Create a job posting
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response newJob(
			@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			@FormParam("closingDate") String closingDate,
			@FormParam("salary") String salary,
			@FormParam("positionType") String positionType,
			@FormParam("location") String location,
			@FormParam("description") String description,
			@FormParam("status") String status
	) {
		if (!Security.instance.isRightKey(orsKey) || !Security.instance.isManager(shortKey)) {
			throw new ForbiddenException("User does not have permission");
		}
		if (!validateInput(closingDate, salary, positionType, location, description, status, null)) {
			throw new BadRequestException("Invalid form parameters");
		}
		String id = UUID.randomUUID().toString();
		Job p = new Job();
		p.set_jobId(id);
		p.setClosingDate(closingDate);
		p.setSalary(Integer.parseInt(salary));
		p.setPositionType(positionType);
		p.setLocation(location);
		p.setDescription(description);
		p.setStatus(RecruitmentStatus.valueOf(status));
		JobsDao.instance.create(p);
		
		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		builder.path(id);
		return Response.created(builder.build()).entity(p).build();
	}

	// Return a list of job postings matching searching terms
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getJobs(
			@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			@QueryParam("closingDateFrom") String closingDateFrom,
			@QueryParam("closingDateTo") String closingDateTo,
			@QueryParam("salaryFrom") String salaryFrom,
			@QueryParam("salaryTo") String salaryTo,
			@QueryParam("positionType") String positionType,
			@QueryParam("location") String location,
			@QueryParam("description") String description,
			@QueryParam("status") String status,
			@QueryParam("assignedTeam") String assignedTeam
	) {
		if (!Security.instance.isInternalUser(orsKey, shortKey)) {
			// External users can only see open jobs
			status = RecruitmentStatus.CREATED.toString();
			assignedTeam = null;
		}
		if (!validateSearchInput(
				closingDateFrom,
				closingDateTo,
				salaryFrom,
				salaryTo,
				positionType,
				location,
				description,
				status,
				assignedTeam)) {
			throw new BadRequestException("Invalid form parameters");
		}
		int salaryFromNum = Integer.MIN_VALUE, salaryToNum = Integer.MAX_VALUE;
		if (salaryFrom != null) {
			salaryFromNum = Integer.parseInt(salaryFrom);
		}
		if (salaryTo != null) {
			salaryToNum = Integer.parseInt(salaryTo);
		}
		List<Job> list = JobsDao.instance.search(
				closingDateFrom,
				closingDateTo,
				salaryFromNum,
				salaryToNum,
				positionType,
				location,
				description,
				status,
				assignedTeam);
		if (list == null) {
			throw new NotFoundException("Job posting list not found");
		} else {
			GenericEntity<List<Job>> returnList = new GenericEntity<List<Job>>(list) {};
			return Response.ok(returnList).build();
		}
	}
	
	// Return the job posting with specified id
	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getJob(@PathParam("id") String id) {
		Job p = JobsDao.instance.getById(id);
		return Response.ok(p).build();
	}
	
	// Return all applications of the job posting with specified id
	@GET
	@Path("{id}/applications")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getJobApplications(
			@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			@PathParam("id") String id
	) {
		if (!Security.instance.isInternalUser(orsKey, shortKey)) {
			throw new ForbiddenException("User does not have permission");
		}
		List<Application> list = ApplicationsDao.instance.getByJob(id);
		if (list == null) {
			throw new NotFoundException("Application list not found");
		} else {
			GenericEntity<List<Application>> returnList = new GenericEntity<List<Application>>(list) {};
			return Response.ok(returnList).build();
		}
	}
	
	// Update the job posting with specified id
	@PUT
	@Path("{id}")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response putJob(
			@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			@PathParam("id") String id,
			Job updatedJob
	) {
		if (!Security.instance.isRightKey(orsKey) || !Security.instance.isManager(shortKey)) {
			throw new ForbiddenException("User does not have permission");
		}
		updatedJob.set_jobId(id);
		JobsDao.instance.update(updatedJob);
		return Response.noContent().build();
	}
	
	// Delete the job posting with specified id
	@DELETE
	@Path("{id}")
	public Response deleteJob(
			@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			@PathParam("id") String id
	) {
		if (!Security.instance.isRightKey(orsKey) || !Security.instance.isManager(shortKey)) {
			throw new ForbiddenException("User does not have permission");
		}
		JobsDao.instance.delete(id);
		return Response.noContent().build();
	}
	
    private boolean validateInput(
    		String closingDate,
    		String salary,
    		String positionType,
    		String location,
    		String description,
    		String status,
    		String assignedTeam) {
    	if (closingDate == null || closingDate.isEmpty()) {
    		return false;
    	}
    	try {
			Date d = dateFormat.parse(closingDate);
	    	if (!dateFormat.format(d).equals(closingDate)) {
	            return false;
	        }
		} catch (ParseException e) {
			return false;
		}
    	if (salary == null || salary.isEmpty()) {
    		return false;
    	}
    	try {
			Integer.parseInt(salary);
		} catch (NumberFormatException e) {
			return false;
		}
    	if (positionType == null || positionType.isEmpty()) {
    		return false;
    	}
    	if (location == null || location.isEmpty()) {
    		return false;
    	}
    	if (description == null) {
    		return false;
    	}
    	if (status == null || status.isEmpty()) {
    		return false;
    	}
    	try {
			RecruitmentStatus.valueOf(status);
		} catch (Exception e) {
			return false;
		}
    	if (assignedTeam != null && UsersDao.instance.getByHireTeam(assignedTeam).size() <= 0) {
			return false;
    	}
    	return true;
    }
    
    private boolean validateSearchInput(
			String closingDateFrom,
			String closingDateTo,
			String salaryFrom,
			String salaryTo,
			String positionType,
			String location,
			String description,
			String status,
			String assignedTeam) {
    	if (closingDateFrom != null) {
        	try {
    			Date d = dateFormat.parse(closingDateFrom);
    	    	if (!dateFormat.format(d).equals(closingDateFrom)) {
    	            return false;
    	        }
    		} catch (ParseException e) {
    			return false;
    		}
    	}
    	if (closingDateTo != null) {
        	try {
    			Date d = dateFormat.parse(closingDateTo);
    	    	if (!dateFormat.format(d).equals(closingDateTo)) {
    	            return false;
    	        }
    		} catch (ParseException e) {
    			return false;
    		}
    	}
    	if (salaryFrom != null) {
        	try {
    			Integer.parseInt(salaryFrom);
    		} catch (NumberFormatException e) {
    			return false;
    		}
    	}
    	if (salaryTo != null) {
        	try {
    			Integer.parseInt(salaryTo);
    		} catch (NumberFormatException e) {
    			return false;
    		}
    	}
    	if (status != null) {
        	try {
    			RecruitmentStatus.valueOf(status);
    		} catch (Exception e) {
    			return false;
    		}
    	}
    	if (assignedTeam != null && UsersDao.instance.getByHireTeam(assignedTeam).size() <= 0) {
			return false;
    	}
    	return true;
    }
	
}
