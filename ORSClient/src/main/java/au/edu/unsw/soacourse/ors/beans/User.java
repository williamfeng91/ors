package au.edu.unsw.soacourse.ors.beans;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Entry")
public class User {

	@XmlAccessorType(XmlAccessType.FIELD)
	private static class Login {
		@XmlElement(name = "_uid")
		private String _uid;
		@XmlElement(name = "_pwd")
		private String _pwd;
		@XmlElement(name = "ShortKey")
		private String ShortKey;
		public String get_uid() {
			return _uid;
		}
		public void set_uid(String _uid) {
			this._uid = _uid;
		}
		public String get_pwd() {
			return _pwd;
		}
		public void set_pwd(String _pwd) {
			this._pwd = _pwd;
		}
		public String getShortKey() {
			return ShortKey;
		}
		public void setShortKey(String shortKey) {
			ShortKey = shortKey;
		}
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	private static class Details {
		private String LastName;
		private String FirstName;
		private String Role;
		private String Department;
		public String getLastName() {
			return LastName;
		}
		public void setLastName(String lastName) {
			LastName = lastName;
		}
		public String getFirstName() {
			return FirstName;
		}
		public void setFirstName(String firstName) {
			FirstName = firstName;
		}
		public String getRole() {
			return Role;
		}
		public void setRole(String role) {
			Role = role;
		}
		public String getDepartment() {
			return Department;
		}
		public void setDepartment(String department) {
			Department = department;
		}
	}
	
	@XmlElement(name = "Login")
	private Login login;
	@XmlElement(name = "Details")
	private Details details;

    public User() {

    }
    
    public Login getLogin() {
		return login;
	}

//    @XmlElement(name = "Login")
	public void setLogin(Login login) {
		this.login = login;
	}

	public Details getDetails() {
		return details;
	}

//	@XmlElement(name = "Details")
	public void setDetails(Details details) {
		this.details = details;
	}

	public String get_uid() {
    	return login.get_uid();
    }
    
	public void set_uid(String _uid) {
		login.set_uid(_uid);
	}
	
	public String get_pwd() {
		return login.get_pwd();
	}
	
	public void set_pwd(String _pwd) {
		login.set_pwd(_pwd);
	}
	
	public String getShortKey() {
		return login.getShortKey();
	}
	
	public void setShortKey(String shortKey) {
		login.setShortKey(shortKey);
	}
	
	public String getLastName() {
		return details.getLastName();
	}
	
	public void setLastName(String lastName) {
		details.setLastName(lastName);
	}
	
	public String getFirstName() {
		return details.getFirstName();
	}
	
	public void setFirstName(String firstName) {
		details.setFirstName(firstName);
	}
	
	public String getRole() {
		return details.getRole();
	}
	
	public void setRole(String role) {
		details.setRole(role);
	}
	
	public String getDepartment() {
		return details.getDepartment();
	}
	
	public void setDepartment(String department) {
		details.setDepartment(department);
	}
	
}
