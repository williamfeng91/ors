package au.edu.unsw.soacourse.ors;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
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

@Path("/reviews")
public class ReviewsResource {
	// Allows to insert contextual objects into the class, 
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	// Create a review
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response newReview(
			@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			@FormParam("_appId") String _appId,
			@FormParam("_uId") String _uId,
			@FormParam("comments") String comments,
			@FormParam("decision") String decision
	) {
		if (!Security.instance.isRightKey(orsKey) || !Security.instance.isReviewer(shortKey)) {
			throw new ForbiddenException("User does not have permission");
		}
		if (!validateInput(_appId, _uId, comments, decision)) {
			throw new BadRequestException("Invalid form parameters");
		}
		String id = UUID.randomUUID().toString();
		Review r = new Review();
		r.set_reviewId(id);
		r.set_appId(_appId);
		r.set_uId(_uId);
		r.setComments(comments);
		r.setDecision(ReviewDecision.valueOf(decision));
		ReviewsDao.instance.create(r);
		
		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
		builder.path(id);
		return Response.created(builder.build()).entity(r).build();
	}

	// Return a list of all reviews
	@GET
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getReviews(
			@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey) {
		if (!Security.instance.isInternalUser(orsKey, shortKey)) {
			throw new ForbiddenException("User does not have permission");
		}
		List<Review> list = ReviewsDao.instance.getAll();
		if (list == null) {
			throw new NotFoundException("Review list not found");
		} else {
			GenericEntity<List<Review>> returnList = new GenericEntity<List<Review>>(list) {};
			return Response.ok(returnList).build();
		}
	}
	
	// Return the review with specified id
	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getReview(
			@HeaderParam("ORSKey") String orsKey,
			@HeaderParam("ShortKey") String shortKey,
			@PathParam("id") String id) {
		if (!Security.instance.isInternalUser(orsKey, shortKey)) {
			throw new ForbiddenException("User does not have permission");
		}
		Review r = ReviewsDao.instance.getById(id);
		return Response.ok(r).build();
	}
	
    private boolean validateInput(
    		String _appId,
    		String _uId,
    		String comments,
    		String decision) {
    	if (_appId == null || _appId.isEmpty()) {
    		return false;
    	}
    	try {
			ApplicationsDao.instance.getById(_appId);
		} catch (NotFoundException e) {
			return false;
		}
    	if (_uId == null || _uId.isEmpty()) {
    		return false;
    	}
    	try {
			UsersDao.instance.getById(_uId);
		} catch (NotFoundException e) {
			return false;
		}
    	if (comments == null || comments.isEmpty()) {
    		return false;
    	}
    	if (decision == null || decision.isEmpty()) {
    		return false;
    	}
    	try {
			ReviewDecision.valueOf(decision);
		} catch (Exception e) {
			return false;
		}
    	return true;
    }
	
}
