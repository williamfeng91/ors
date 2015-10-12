package au.edu.unsw.soacourse.ors.beans;

import java.util.ArrayList;

import au.edu.unsw.soacourse.ors.common.ReviewDecision;

public class DetailedApplication extends Application {

	private AutoCheckResult autoCheckResult;
	private ArrayList<Review> reviews;
	
	public DetailedApplication() {
		super();
	}
    
    public boolean isShortlistedByAllReviewers() {
    	if (reviews == null || reviews.isEmpty()) {
    		return false;
    	}
    	for (Review r : reviews) {
    		if (r.getDecision().equals(ReviewDecision.REJECT)) {
    			return false;
    		}
    	}
    	return true;
    }
	
    public boolean hasBeenReviewedBy(String uId) {
    	if (reviews == null || reviews.isEmpty()) {
    		return false;
    	}
    	for (Review r : reviews) {
    		if (r.get_uId().equals(uId)) {
    			return true;
    		}
    	}
    	return false;
    }
	
	public DetailedApplication(Application a) {
		super(a);
	}

	public AutoCheckResult getAutoCheckResult() {
		return autoCheckResult;
	}

	public void setAutoCheckResult(AutoCheckResult autoCheckResult) {
		this.autoCheckResult = autoCheckResult;
	}

	public ArrayList<Review> getReviews() {
		return reviews;
	}

	public void setReviews(ArrayList<Review> reviews) {
		this.reviews = reviews;
	}

}
