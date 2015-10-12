package au.edu.unsw.soacourse.ors.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import au.edu.unsw.soacourse.autocheck.AutoCheckRequest;
import au.edu.unsw.soacourse.autocheck.AutoCheckResponse;
import au.edu.unsw.soacourse.autocheck.AutoCheckServiceProcessPortType;
import au.edu.unsw.soacourse.ors.beans.*;
import au.edu.unsw.soacourse.ors.business.DataService;
import au.edu.unsw.soacourse.ors.common.ApplicationStatus;
import au.edu.unsw.soacourse.ors.dao.ApplicationsDao;
import au.edu.unsw.soacourse.ors.dao.AutoCheckResultsDao;
import au.edu.unsw.soacourse.ors.dao.JobsDao;
import au.edu.unsw.soacourse.ors.dao.ReviewsDao;

@Controller
public class ApplicationController {
	
	@Autowired
	private AutoCheckServiceProcessPortType autoCheckService;
	
	private static final String ORSKEY = "i-am-ors";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final String TODAY = DATE_FORMAT.format(new Date());
	
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
		String licenseNo = request.getParameter("licenseNo");
		String fullName = request.getParameter("fullName");
		String postcode = request.getParameter("postcode");
		String cv = request.getParameter("cv");
		String resume = request.getParameter("resume");

		Application a = new Application();
		a.set_jobId(_jobId);
		a.setLicenseNo(licenseNo);
		a.setFullName(fullName);
		a.setPostcode(postcode);
		a.setCv(cv);
		a.setResume(resume);
		if (!validateInput(_jobId, licenseNo, fullName, postcode, cv, resume)) {
			Job j = JobsDao.instance.getById(_jobId);
			model.addAttribute("job", j);
			model.addAttribute("errorMsg", "Invalid form data");
			model.addAttribute("application", a);
			return "addApplication";
		}
		try {
			Application newApplication = ApplicationsDao.instance.create(
					_jobId,
					licenseNo,
					fullName,
					postcode,
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
			List<DetailedApplication> list = new ArrayList<DetailedApplication>();
			List<Application> applications = ApplicationsDao.instance.getAll(ORSKEY, user.getShortKey());
			for (Application a : applications) {
				DetailedApplication da = new DetailedApplication(a);
				da.setAutoCheckResult(AutoCheckResultsDao.instance.getByApplication(ORSKEY, user.getShortKey(), da.get_appId()));
				da.setReviews((ArrayList) ReviewsDao.instance.getByApplication(ORSKEY, user.getShortKey(), da.get_appId()));
				list.add(da);
			}
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
			List<DetailedApplication> list = new ArrayList<DetailedApplication>();
			List<Application> applications = ApplicationsDao.instance.getByJob(ORSKEY, user.getShortKey().toString(), jobId);
			for (Application a : applications) {
				DetailedApplication da = new DetailedApplication(a);
				da.setAutoCheckResult(AutoCheckResultsDao.instance.getByApplication(ORSKEY, user.getShortKey(), da.get_appId()));
				da.setReviews((ArrayList) ReviewsDao.instance.getByApplication(ORSKEY, user.getShortKey(), da.get_appId()));
				list.add(da);
			}
			Job j = JobsDao.instance.getById(jobId);
			model.addAttribute("applications", list);
			model.addAttribute("job", j);
			model.addAttribute("today", TODAY);
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
	
	@RequestMapping("/applications/{appId}/autoCheckResult")
	public String visitAutoCheckResultPage(@PathVariable String appId, HttpServletRequest request, ModelMap model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null || !user.getRole().equals("manager")) {
			model.addAttribute("errorMsg", "User has no permission");
			return "login";
		}
		try {
			Application a = ApplicationsDao.instance.getById(appId);
			AutoCheckResult result = AutoCheckResultsDao.instance.getByApplication(ORSKEY, user.getShortKey(), appId);
			if (result != null) {
				model.addAttribute("autoCheckResult", result);
			}
			model.addAttribute("application", a);
			return "autoCheckResult";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value="/applications/{appId}/doAutoCheck")
	public String doAutoCheck(@PathVariable String appId, HttpServletRequest request, ModelMap model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null || !user.getRole().equals("manager")) {
			model.addAttribute("errorMsg", "User has no permission");
			return "login";
		}
		try {
			Application a = ApplicationsDao.instance.getById(appId);
			AutoCheckRequest req = new AutoCheckRequest();
			req.setLicenseNo(a.getLicenseNo());
			req.setFullName(a.getFullName());
			req.setPostcode(a.getPostcode());
			AutoCheckResponse response = autoCheckService.check(req);
			String pdvResult = response.getPdvResult();
			String crvResult = response.getCrvResult();
			// save result to API
			AutoCheckResult newAutoCheckResult = new AutoCheckResult();
			newAutoCheckResult.set_appId(appId);
			newAutoCheckResult.setPdvResult(pdvResult);
			newAutoCheckResult.setCrvResult(crvResult);
			AutoCheckResultsDao.instance.create(ORSKEY, user.getShortKey(), newAutoCheckResult);
			return "redirect:/applications/" + appId + "/autoCheckResult";
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
		String licenseNo = request.getParameter("licenseNo");
		String fullName = request.getParameter("fullName");
		String postcode = request.getParameter("postcode");
		String cv = request.getParameter("cv");
		String resume = request.getParameter("resume");

		Application updatedApplication = ApplicationsDao.instance.getById(id);
		updatedApplication.set_jobId(_jobId);
		updatedApplication.setLicenseNo(licenseNo);
		updatedApplication.setFullName(fullName);
		updatedApplication.setPostcode(postcode);
		updatedApplication.setCv(cv);
		updatedApplication.setResume(resume);
		if (!validateInput(_jobId, licenseNo, fullName, postcode, cv, resume)) {
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
    		String licenseNo,
    		String fullName,
    		String postcode,
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
    	if (licenseNo == null || licenseNo.isEmpty()) {
    		return false;
    	}
    	if (fullName == null || fullName.isEmpty()) {
    		return false;
    	}
    	if (postcode == null || postcode.isEmpty()) {
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