package au.edu.unsw.soacourse.ors.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import au.edu.unsw.soacourse.ors.beans.*;
import au.edu.unsw.soacourse.ors.common.ApplicationStatus;
import au.edu.unsw.soacourse.ors.common.ReviewDecision;
import au.edu.unsw.soacourse.ors.dao.ReviewsDao;
import au.edu.unsw.soacourse.ors.dao.ApplicationsDao;
import au.edu.unsw.soacourse.ors.dao.UsersDao;

@Controller
public class ReviewController {
	
	private static final String ORSKEY = "i-am-ors";
	
	@RequestMapping("/applications/{appId}/review")
	public String visitNewReviewPage(@PathVariable String appId, HttpServletRequest request, ModelMap model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null || !user.getRole().equals("reviewer")) {
			model.addAttribute("errorMsg", "User has no permission");
			return "login";
		}
		try {
			Application a = ApplicationsDao.instance.getById(appId);
			model.addAttribute("application", a);
			model.addAttribute("decisions", ReviewDecision.values());
			return "addReview";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value="/applications/{appId}/reviews", method={RequestMethod.POST})
	public String createNewReview(@PathVariable String appId, HttpServletRequest request, ModelMap model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null || !user.getRole().equals("reviewer")) {
			model.addAttribute("errorMsg", "User has no permission");
			return "login";
		}
		String comments = request.getParameter("comments");
		String decision = request.getParameter("decision");

		Review r = new Review();
		r.set_appId(appId);
		r.setComments(comments);
		r.setDecision(ReviewDecision.valueOf(decision));
		Application a = ApplicationsDao.instance.getById(appId);
		if (!validateInput(appId, comments, decision)) {
			model.addAttribute("errorMsg", "Invalid form data");
			model.addAttribute("review", r);
			model.addAttribute("application", a);
			model.addAttribute("decisions", ReviewDecision.values());
			return "addReview";
		}
		try {
			Review newReview = ReviewsDao.instance.create(
					ORSKEY,
					user.getShortKey(),
					appId,
					user.get_uid(),
					comments,
					decision);
			if (newReview != null
					&& ReviewsDao.instance.getByApplication(ORSKEY, user.getShortKey(), appId).size()
						== UsersDao.instance.getByHireTeam(user.getDepartment()).size()) {
				// if everyone in the team has reviewed on an application, proceed to next application stage
				a.setStatus(ApplicationStatus.REVIEWED);
				ApplicationsDao.instance.update(a);
			}
			return "redirect:/jobs/" + a.get_jobId() + "/applications/";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value="/applications/{appId}/reviews", method={RequestMethod.GET})
	public String visitApplicationPage(@PathVariable String appId, HttpServletRequest request, ModelMap model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			model.addAttribute("errorMsg", "User has no permission");
			return "login";
		}
		try {
			List<Review> list = ReviewsDao.instance.getByApplication(ORSKEY, user.getShortKey(), appId);
			Application a = ApplicationsDao.instance.getById(appId);
			model.addAttribute("reviews", list);
			model.addAttribute("application", a);
			return "reviews";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "error";
		}
	}
	
	private boolean validateInput(
    		String _appId,
    		String comments,
    		String decision) {
    	if (_appId == null || _appId.isEmpty()) {
    		return false;
    	}
    	try {
			ApplicationsDao.instance.getById(_appId);
		} catch (Exception e) {
			return false;
		}
    	if (comments == null || comments.isEmpty()) {
    		return false;
    	}
    	if (decision == null || decision.isEmpty()) {
    		return false;
    	}
    	try {
    		ReviewDecision.valueOf(decision);
    	} catch (Exception e) {
    		return false;
    	}
    	return true;
    }
}