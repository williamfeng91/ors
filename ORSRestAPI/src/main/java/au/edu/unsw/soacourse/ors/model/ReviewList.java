package au.edu.unsw.soacourse.ors.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "reviews")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReviewList {
	
	@XmlElement(name = "review")
	private List<Review> reviewList;

	public List<Review> getReviewList() {
		return reviewList;
	}

	public void setReviewList(List<Review> reviewList) {
		this.reviewList = reviewList;
	}

}
