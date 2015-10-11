package au.edu.unsw.soacourse.ors;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import au.edu.unsw.soacourse.ors.dao.*;
import au.edu.unsw.soacourse.ors.model.*;

@Path("/applications")
public class ApplicationsResource {
	// Allows to insert contextual objects into the class, 
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	// Create a application
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response newApplication(
			@FormParam("_jobId") String _jobId,
			@FormParam("licenseNo") String licenseNo,
			@FormParam("fullName") String fullName,
			@FormParam("postcode") String postcode,
			@FormParam("cv") String cv,
			@FormParam("resume") String resume,
			@FormParam("status") String status
	) {
		if (!validateInput(_jobId, licenseNo, fullName, postcode, cv, resume, status)) {
			throw new BadRequestException("Invalid form parameters");
		}
		String id = UUID.randomUUID().toString();
		Application a = new Application();
		a.set_appId(id);
		a.set_jobId(_jobId);
		a.setLicenseNo(licenseNo);
		a.setFullName(fullName);
		a.setPostcode(postcode);
		a.setCv(cv);
		a.setResume(resume);
		a.setStatus(ApplicationStatus.valueOf(status));
		ApplicationsDao.instance.create(a);
		
		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		builder.path(id);
		return Response.created(builder.build()).entity(a).build();
	}

	// Return a list of all applications
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getApplications() {
		List<Application> list = ApplicationsDao.instance.getAll();
		if (list == null) {
			throw new NotFoundException("Application list not found");
		} else {
			GenericEntity<List<Application>> returnList = new GenericEntity<List<Application>>(list) {};
			return Response.ok(returnList).build();
		}
	}
	
	// Return the application with specified id
	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getApplication(@PathParam("id") String id) {
		Application a = ApplicationsDao.instance.getById(id);
		return Response.ok(a).build();
	}
	
	// Return the reviews of the application with specified id
	@GET
	@Path("{id}/reviews")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getApplicationReviews(@PathParam("id") String id) {
		List<Review> list = ReviewsDao.instance.getByApplication(id);
		if (list == null) {
			throw new NotFoundException("Review list not found");
		} else {
			GenericEntity<List<Review>> returnList = new GenericEntity<List<Review>>(list) {};
			return Response.ok(returnList).build();
		}
	}
	
	// Update the application with specified id
	@PUT
	@Path("{id}")
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response putApplication(@PathParam("id") String id, Application updatedApplication) {
		updatedApplication.set_appId(id);
		ApplicationsDao.instance.update(updatedApplication);
		return Response.noContent().build();
	}
	
	// Delete the application with specified id
	@DELETE
	@Path("{id}")
	public Response deleteApplication(@PathParam("id") String id) {
		ApplicationsDao.instance.delete(id);
		return Response.noContent().build();
	}
	
    private boolean validateInput(
    		String _jobId,
    		String licenseNo,
    		String fullName,
    		String postcode,
    		String cv,
    		String resume,
    		String status) {
    	if (_jobId == null || _jobId.isEmpty()) {
    		return false;
    	}
    	try {
			JobsDao.instance.getById(_jobId);
		} catch (NotFoundException e) {
			return false;
		}
    	if (licenseNo == null || licenseNo.isEmpty()) {
    		return false;
    	}
    	if (fullName == null || fullName.isEmpty()) {
    		return false;
    	}
    	if (postcode == null || postcode.isEmpty()) {
    		return false;
    	}
    	if (cv == null || cv.isEmpty()) {
    		return false;
    	}
    	if (resume == null || resume.isEmpty()) {
    		return false;
    	}
    	if (status == null || status.isEmpty()) {
    		return false;
    	}
    	try {
			ApplicationStatus.valueOf(status);
		} catch (Exception e) {
			return false;
		}
    	return true;
    }
	
}
