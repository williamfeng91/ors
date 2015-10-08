package au.edu.unsw.soacourse.ors.beans;

import au.edu.unsw.soacourse.ors.common.ApplicationStatus;

public class Application {

    private String _appId;
    private String _jobId;
    private String name;
    private String cv;
    private String resume;
    private ApplicationStatus status;
    
	public Application() {
		
	}

	public String get_appId() {
		return _appId;
	}

	public void set_appId(String _appId) {
		this._appId = _appId;
	}

	public String get_jobId() {
		return _jobId;
	}

	public void set_jobId(String _jobId) {
		this._jobId = _jobId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCv() {
		return cv;
	}

	public void setCv(String cv) {
		this.cv = cv;
	}

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}
}
