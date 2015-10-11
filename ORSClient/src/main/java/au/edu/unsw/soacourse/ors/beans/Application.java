package au.edu.unsw.soacourse.ors.beans;

import au.edu.unsw.soacourse.ors.common.ApplicationStatus;

public class Application {

    private String _appId;
    private String _jobId;
    private String licenseNo;
    private String fullName;
    private String postcode;
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

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
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
