package au.edu.unsw.soacourse.ors;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import au.edu.unsw.soacourse.ors.dao.ReviewsDao;
import au.edu.unsw.soacourse.ors.dao.UsersDao;
import au.edu.unsw.soacourse.ors.model.*;

@Path("/users")
public class UsersResource {
	// Allows to insert contextual objects into the class, 
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	// Return a list of users matching getByHireTeaming terms
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getUsers(@QueryParam("hireTeam") String hireTeam) {
		List<User> list = UsersDao.instance.getByHireTeam(hireTeam);
		if (list == null) {
			throw new NotFoundException("User list not found");
		} else {
			GenericEntity<List<User>> returnList = new GenericEntity<List<User>>(list) {};
			return Response.ok(returnList).build();
		}
	}
	
	// Return the user with specified id
	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getUser(@PathParam("id") String id) {
		User u = UsersDao.instance.getById(id);
		return Response.ok(u).build();
	}
	
	// Return the reviews written by the reviewers with specified id
	@GET
	@Path("{id}/reviews")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getApplicationReviews(@PathParam("id") String id) {
		List<Review> list = ReviewsDao.instance.getByReviewer(id);
		if (list == null) {
			throw new NotFoundException("Review list not found");
		} else {
			GenericEntity<List<Review>> returnList = new GenericEntity<List<Review>>(list) {};
			return Response.ok(returnList).build();
		}
	}
	
}
