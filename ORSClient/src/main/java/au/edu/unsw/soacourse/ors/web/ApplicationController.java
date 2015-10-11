package au.edu.unsw.soacourse.ors.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import au.edu.unsw.soacourse.ors.beans.*;
import au.edu.unsw.soacourse.ors.common.ApplicationStatus;
import au.edu.unsw.soacourse.ors.dao.ApplicationsDao;
import au.edu.unsw.soacourse.ors.dao.JobsDao;
import au.edu.unsw.soacourse.ors.dao.ReviewsDao;

@Controller
public class ApplicationController {
	
	private static final String ORSKEY = "i-am-ors";
	
	@RequestMapping("/jobs/{jobId}/apply")
	public String visitNewApplicationPage(@PathVariable String jobId, ModelMap model) {
		try {
			Job j = JobsDao.instance.getById(jobId);
			model.addAttribute("job", j);
			return "addApplication";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value="/applications", method={RequestMethod.POST})
	public String createNewApplication(HttpServletRequest request, ModelMap model) {
		String _jobId = request.getParameter("_jobId");
		String name = request.getParameter("name");
		String cv = request.getParameter("cv");
		String resume = request.getParameter("resume");

		Application a = new Application();
		a.set_jobId(_jobId);
		a.setName(name);
		a.setCv(cv);
		a.setResume(resume);
		if (!validateInput(_jobId, name, cv, resume)) {
			Job j = JobsDao.instance.getById(_jobId);
			model.addAttribute("job", j);
			model.addAttribute("errorMsg", "Invalid form data");
			model.addAttribute("application", a);
			return "addApplication";
		}
		try {
			Application newApplication = ApplicationsDao.instance.create(
					_jobId,
					name,
					cv,
					resume,
					ApplicationStatus.CREATED.toString());
			return "redirect:/applications/" + newApplication.get_appId();
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "error";
		}
	}

	@RequestMapping(value="/applications", method={RequestMethod.GET})
	public String visitApplicationListPage(HttpServletRequest request, ModelMap model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null || !user.getRole().equals("manager")) {
			model.addAttribute("errorMsg", "User has no permission");
			return "login";
		}
		try {
			List<Application> list;
			list = ApplicationsDao.instance.getAll(ORSKEY, user.getShortKey().toString());
			model.addAttribute("applications", list);
			return "applications";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "error";
		}
	}

	@RequestMapping(value="jobs/{jobId}/applications")
	public String visitJobApplicationListPage(@PathVariable String jobId, HttpServletRequest request, ModelMap model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			model.addAttribute("errorMsg", "User has no permission");
			return "login";
		}
		try {
			List<Application> list = ApplicationsDao.instance.getByJob(ORSKEY, user.getShortKey().toString(), jobId);
			for (Application a: list) {
				if (user.getRole().equals("reviewer")
						&& ReviewsDao.instance.hasBeenReviewedBy(ORSKEY, user.getShortKey(), a.get_appId(), user.get_uid())) {
					// mark that the current user has reviewed this application
					a.setStatus(ApplicationStatus.REVIEWED);
				}
			}
			Job j = JobsDao.instance.getById(jobId);
			model.addAttribute("applications", list);
			model.addAttribute("job", j);
			return "applications";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value="/applications/{id}")
	public String visitApplicationPage(@PathVariable String id, ModelMap model) {
		try {
			Application a = ApplicationsDao.instance.getById(id);
			Job j = JobsDao.instance.getById(a.get_jobId());
			model.addAttribute("application", a);
			model.addAttribute("job", j);
			return "applicationDetails";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping("/applications/{id}/edit")
	public String visitEditApplicationPage(@PathVariable String id, ModelMap model) {
		try {
			Application a = ApplicationsDao.instance.getById(id);
			Job j = JobsDao.instance.getById(a.get_jobId());
			model.addAttribute("application", a);
			model.addAttribute("job", j);
			return "editApplication";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value="/applications/{id}/update", method={RequestMethod.POST})
	public String updateApplication(@PathVariable String id, HttpServletRequest request, ModelMap model) {
		String _jobId = request.getParameter("_jobId");
		String name = request.getParameter("name");
		String cv = request.getParameter("cv");
		String resume = request.getParameter("resume");

		Application updatedApplication = ApplicationsDao.instance.getById(id);
		updatedApplication.set_jobId(_jobId);
		updatedApplication.setName(name);
		updatedApplication.setCv(cv);
		updatedApplication.setResume(resume);
		if (!validateInput(_jobId, name, cv, resume)) {
			model.addAttribute("errorMsg", "Invalid form data");
			model.addAttribute("application", updatedApplication);
			return "editApplication";
		}
		try {
			ApplicationsDao.instance.update(updatedApplication);
			return "redirect:/applications/" + id;
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping("/applications/{appId}/acceptInvitation")
	public String acceptInvitation(@PathVariable String appId, ModelMap model) {
		try {
			Application a = ApplicationsDao.instance.getById(appId);
			a.setStatus(ApplicationStatus.ACCEPTED_INVITATION);
			ApplicationsDao.instance.update(a);
			return "redirect:/applications/" + appId;
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping("/applications/{appId}/rejectInvitation")
	public String rejectInvitation(@PathVariable String appId, ModelMap model) {
		try {
			Application a = ApplicationsDao.instance.getById(appId);
			a.setStatus(ApplicationStatus.FINALISED);
			ApplicationsDao.instance.update(a);
			return "redirect:/applications/" + appId;
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value="/applications/{id}/delete")
	public String deleteApplication(@PathVariable String id, ModelMap model) {
		try {
			ApplicationsDao.instance.delete(id);
			return "redirect:/jobs";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "error";
		}
	}
	
	private boolean validateInput(
    		String _jobId,
    		String name,
    		String cv,
    		String resume) {
    	if (_jobId == null || _jobId.isEmpty()) {
    		return false;
    	}
    	try {
			JobsDao.instance.getById(_jobId);
		} catch (Exception e) {
			return false;
		}
    	if (name == null || name.isEmpty()) {
    		return false;
    	}
    	if (cv == null || cv.isEmpty()) {
    		return false;
    	}
    	if (resume == null || resume.isEmpty()) {
    		return false;
    	}
    	return true;
    }
}