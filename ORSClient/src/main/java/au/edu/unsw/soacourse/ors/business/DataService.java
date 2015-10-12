package au.edu.unsw.soacourse.ors.business;

import java.util.List;

import au.edu.unsw.soacourse.ors.beans.*;
import au.edu.unsw.soacourse.ors.common.*;
import au.edu.unsw.soacourse.ors.dao.*;


public enum DataService {
    instance;

    private DataService() {
    }
    
    /**
     * Check if all applications of a job have been reviewed
     * @param orsKey
     * @param shortKey
     * @param jobId
     * @return
     */
    public boolean allApplicationsReviewed(String orsKey, String shortKey, String jobId) {
		List<Application> al = ApplicationsDao.instance.getByJob(orsKey, shortKey, jobId);
		if (al.isEmpty()) {
			return false;
		}
		for (Application item : al) {
			if (!item.getStatus().equals(ApplicationStatus.REVIEWED)
					&& !item.getStatus().equals(ApplicationStatus.ARCHIVED)) {
				return false;
			}
		}
    	return true;
    }
    
    /**
     * Check if all applications of a job have been auto-checked
     * @param orsKey
     * @param shortKey
     * @param jobId
     * @return
     */
    public boolean allApplicationsAutoChecked(String orsKey, String shortKey, String jobId) {
    	List<Application> applications = ApplicationsDao.instance.getByJob(orsKey, shortKey, jobId);
    	for (Application a : applications) {
    		try {
    			if (AutoCheckResultsDao.instance.getByApplication(orsKey, shortKey, a.get_appId()) == null) {
    				return false;
    			}
    		} catch (Exception e) {
    			return false;
    		}
    	}
    	return true;
    }
    
    /**
     * Check if a job has received applications
     * @param orsKey
     * @param shortKey
     * @param jobId
     * @return
     */
    public boolean hasReceivedApplication(String orsKey, String shortKey, String jobId) {
    	try {
    		List<Application> applications = ApplicationsDao.instance.getByJob(orsKey, shortKey, jobId);
    		return applications.size() > 0;
    	} catch (Exception e) {
    		return false;
    	}
    }

}
