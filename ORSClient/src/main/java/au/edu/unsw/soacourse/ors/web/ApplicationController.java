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
import au.edu.unsw.soacourse.ors.dao.ApplicationsDao;
import au.edu.unsw.soacourse.ors.dao.JobsDao;

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
		String personalDetails = request.getParameter("personalDetails");
		String cv = request.getParameter("cv");
		String resume = request.getParameter("resume");

		Application a = new Application();
		a.set_jobId(_jobId);
		a.setPersonalDetails(personalDetails);
		a.setCv(cv);
		a.setResume(resume);
		if (!validateInput(_jobId, personalDetails, cv, resume)) {
			model.addAttribute("errorMsg", "Invalid form data");
			model.addAttribute("application", a);
			return "addApplication";
		}
		try {
			Application newApplication = ApplicationsDao.instance.create(
					_jobId,
					personalDetails,
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
			List<Application> list;
			list = ApplicationsDao.instance.getByJob(ORSKEY, user.getShortKey().toString(), jobId);
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
		String personalDetails = request.getParameter("personalDetails");
		String cv = request.getParameter("cv");
		String resume = request.getParameter("resume");

		Application updatedApplication = ApplicationsDao.instance.getById(id);
		updatedApplication.set_jobId(_jobId);
		updatedApplication.setPersonalDetails(personalDetails);
		updatedApplication.setCv(cv);
		updatedApplication.setResume(resume);
		if (!validateInput(_jobId, personalDetails, cv, resume)) {
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
    		String personalDetails,
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
    	if (personalDetails == null || personalDetails.isEmpty()) {
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