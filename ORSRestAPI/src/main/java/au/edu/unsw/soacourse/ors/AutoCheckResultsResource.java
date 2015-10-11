package au.edu.unsw.soacourse.ors;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotFoundException;
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
import au.edu.unsw.soacourse.ors.security.Security;

@Path("/autoCheckResults")
public class AutoCheckResultsResource {
	// Allows to insert contextual objects into the class, 
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	// Create/update a autoCheckResult
	@PUT
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response newAutoCheckResult(
			@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			AutoCheckResult newAutoCheckResult) {
		if (!Security.instance.isRightKey(orsKey) || !Security.instance.isManager(shortKey)) {
			throw new ForbiddenException("User does not have permission");
		}
		if (!validateInput(
				newAutoCheckResult.get_autoCheckId(),
				newAutoCheckResult.get_appId(),
				newAutoCheckResult.getPdvResult(),
				newAutoCheckResult.getCrvResult()
		)) {
			throw new BadRequestException("Invalid form parameters");
		}
		try {
			AutoCheckResultsDao.instance.getByApplication(newAutoCheckResult.get_appId());
			AutoCheckResultsDao.instance.update(newAutoCheckResult);
			return Response.noContent().build();
		} catch (NotFoundException e) {
			String id = UUID.randomUUID().toString();
			newAutoCheckResult.set_autoCheckId(id);
			AutoCheckResultsDao.instance.create(newAutoCheckResult);
			UriBuilder builder = uriInfo.getAbsolutePathBuilder();
			builder.path(newAutoCheckResult.get_autoCheckId());
			return Response.created(builder.build()).entity(newAutoCheckResult).build();
		}
	}

	// Return a list of all autoCheckResults
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getAutoCheckResults(
			@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey) {
		if (!Security.instance.isRightKey(orsKey) || !Security.instance.isManager(shortKey)) {
			throw new ForbiddenException("User does not have permission");
		}
		List<AutoCheckResult> list = AutoCheckResultsDao.instance.getAll();
		if (list == null) {
			throw new NotFoundException("AutoCheckResult list not found");
		} else {
			GenericEntity<List<AutoCheckResult>> returnList = new GenericEntity<List<AutoCheckResult>>(list) {};
			return Response.ok(returnList).build();
		}
	}
	
	// Return the autoCheckResult with specified id
	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getAutoCheckResult(
			@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			@PathParam("id") String id) {
		if (!Security.instance.isRightKey(orsKey) || !Security.instance.isManager(shortKey)) {
			throw new ForbiddenException("User does not have permission");
		}
		AutoCheckResult a = AutoCheckResultsDao.instance.getById(id);
		return Response.ok(a).build();
	}
	
    private boolean validateInput(
    		String _autoCheckId,
    		String _appId,
    		String pdvResult,
    		String crvResult) {
    	if (_autoCheckId != null) {
    		try {
    			AutoCheckResultsDao.instance.getById(_autoCheckId);
    		} catch (Exception e) {
    			return false;
    		}
    	}
    	if (_appId == null) {
    		try {
    			ApplicationsDao.instance.getById(_appId);
    		} catch (Exception e) {
    			return false;
    		}
    	}
    	if (pdvResult == null || pdvResult.isEmpty()) {
    		return false;
    	}
    	if (crvResult == null || crvResult.isEmpty()) {
    		return false;
    	}
    	return true;
    }
	
}
