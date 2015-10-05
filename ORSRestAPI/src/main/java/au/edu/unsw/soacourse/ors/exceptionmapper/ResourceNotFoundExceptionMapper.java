package au.edu.unsw.soacourse.ors.exceptionmapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import au.edu.unsw.soacourse.ors.exception.ResourceNotFoundException;
import au.edu.unsw.soacourse.ors.model.ErrorResponse;

public class ResourceNotFoundExceptionMapper
		implements ExceptionMapper<ResourceNotFoundException> {

	public ResourceNotFoundExceptionMapper() {
		
	}
	
	public Response toResponse(ResourceNotFoundException e) {
		ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
		return Response.status(Status.NOT_FOUND)
				.entity(errorResponse)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}
}
