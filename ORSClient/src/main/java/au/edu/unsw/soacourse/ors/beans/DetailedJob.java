package au.edu.unsw.soacourse.ors.beans;

import java.util.ArrayList;

import au.edu.unsw.soacourse.ors.common.ApplicationStatus;
import au.edu.unsw.soacourse.ors.dao.AutoCheckResultsDao;

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

	public void setApplications(String orsKey, String shortKey, ArrayList<Application> applications) {
		this.applications = new ArrayList<DetailedApplication>();
		for (Application a : applications) {
			DetailedApplication da = new DetailedApplication(a);
			da.setAutoCheckResult(AutoCheckResultsDao.instance.getByApplication(orsKey, shortKey, da.get_appId()));
			this.applications.add(da);
		}
	}

    public boolean allApplicationsAutoChecked() {
    	if (applications.isEmpty()) {
			return false;
		}
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
