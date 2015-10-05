package au.edu.unsw.soacourse.ors.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "errorResponse")
public class ErrorResponse {

	private String message;

	public ErrorResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
