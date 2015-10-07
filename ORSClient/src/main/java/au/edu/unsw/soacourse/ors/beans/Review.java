package au.edu.unsw.soacourse.ors.beans;

import au.edu.unsw.soacourse.ors.common.ReviewDecision;

public class Review {

    private String _reviewId;
    private String _appId;
    private String _uId;
    private String comments;
    private ReviewDecision decision;

    public Review() {

    }

	public String get_reviewId() {
		return _reviewId;
	}

	public void set_reviewId(String _reviewId) {
		this._reviewId = _reviewId;
	}

	public String get_appId() {
		return _appId;
	}

	public void set_appId(String _appId) {
		this._appId = _appId;
	}

	public String get_uId() {
		return _uId;
	}

	public void set_uId(String _uId) {
		this._uId = _uId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public ReviewDecision getDecision() {
		return decision;
	}

	public void setDecision(ReviewDecision decision) {
		this.decision = decision;
	}
}
