package au.edu.unsw.soacourse.ors.beans;

import java.util.ArrayList;

import au.edu.unsw.soacourse.ors.common.ApplicationStatus;

public class DetailedJob extends Job {

	private ArrayList<DetailedApplication> applications;
	
	public DetailedJob() {
		super();
	}
	
	public DetailedJob(Job j) {
		super(j);
	}

	public ArrayList<DetailedApplication> getApplications() {
		return applications;
	}

	public void setDetailedApplications(ArrayList<DetailedApplication> applications) {
		this.applications = applications;
	}

	public void setApplications(ArrayList<Application> applications) {
		this.applications = new ArrayList<DetailedApplication>();
		for (Application a : applications) {
			this.applications.add(new DetailedApplication(a));
		}
	}

    public boolean allApplicationsAutoChecked() {
    	for (DetailedApplication a : applications) {
    		if (a.getAutoCheckResult() == null) {
    			return false;
    		}
    	}
    	return true;
    }
	
    public boolean allApplicationsReviewed() {
		if (applications.isEmpty()) {
			return false;
		}
		for (Application a : applications) {
			if (!a.getStatus().equals(ApplicationStatus.REVIEWED)
					&& !a.getStatus().equals(ApplicationStatus.ARCHIVED)) {
				return false;
			}
		}
    	return true;
    }

    public boolean hasReceivedApplication() {
    	return applications != null && !applications.isEmpty();
    }

}
