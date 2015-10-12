package au.edu.unsw.soacourse.ors.beans;

import au.edu.unsw.soacourse.ors.common.RecruitmentStatus;

public class Job {

	private String _jobId;
    private String closingDate;
    private int salary;
    private String positionType;
    private String location;
    private String description;
    private RecruitmentStatus status;
    private String assignedTeam;

    public Job() {

    }
    
    public Job(Job another) {
    	this._jobId = another._jobId;
    	this.closingDate = another.closingDate;
    	this.salary = another.salary;
    	this.positionType = another.positionType;
    	this.location = another.location;
    	this.description = another.description;
    	this.status = another.status;
    	this.assignedTeam = another.assignedTeam;
    }

	public String get_jobId() {
		return _jobId;
	}

	public void set_jobId(String _jobId) {
		this._jobId = _jobId;
	}

	public String getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(String closingDate) {
		this.closingDate = closingDate;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public String getPositionType() {
		return positionType;
	}

	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public RecruitmentStatus getStatus() {
		return status;
	}

	public void setStatus(RecruitmentStatus status) {
		this.status = status;
	}

	public String getAssignedTeam() {
		return assignedTeam;
	}

	public void setAssignedTeam(String assignedTeam) {
		this.assignedTeam = assignedTeam;
	}
}
