package au.edu.unsw.soacourse.ors.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import au.edu.unsw.soacourse.ors.beans.*;
import au.edu.unsw.soacourse.ors.common.RecruitmentStatus;
import au.edu.unsw.soacourse.ors.dao.JobsDao;
import au.edu.unsw.soacourse.ors.dao.UsersDao;

@Controller
public class JobController {
	
	private static final String ORSKEY = "i-am-ors";
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	UsersDao usersDao;
	
	@Autowired
	ServletContext context;
	
	@PostConstruct
	public void setUpUserDB() {
		usersDao = new UsersDao(context);
	}
	
	@RequestMapping("/jobs/new")
	public String visitNewJobPage(HttpServletRequest request, ModelMap model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			model.addAttribute("errorMsg", "User has no permission");
			return "login";
		}
		return "addJob";
	}
	
	@RequestMapping(value="/jobs", method={RequestMethod.POST})
	public String createNewJob(HttpServletRequest request, ModelMap model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			model.addAttribute("errorMsg", "User has no permission");
			return "login";
		}
		String closingDate = request.getParameter("closingDate");
		String salary = request.getParameter("salary");
		String positionType = request.getParameter("positionType");
		String location = request.getParameter("location");
		String description = request.getParameter("description");

		Job j = new Job();
		j.setClosingDate(closingDate);
		j.setSalary(Integer.parseInt(salary));
		j.setPositionType(positionType);
		j.setLocation(location);
		j.setDescription(description);
		if (!validateInput(closingDate, salary, positionType, location, description, null, null)) {
			model.addAttribute("errorMsg", "Invalid form data");
			model.addAttribute("job", j);
			return "addJob";
		}
		try {
			Job newJob = JobsDao.instance.create(
					ORSKEY,
					user.getShortKey(),
					closingDate,
					salary, positionType,
					location,
					description,
					RecruitmentStatus.CREATED.toString(),
					null);
			return "redirect:/jobs/" + newJob.get_jobId();
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "error";
		}
	}

	@RequestMapping(value="/jobs", method={RequestMethod.GET})
	public String visitJobListPage(HttpServletRequest request, ModelMap model) {
		User user = (User) request.getSession().getAttribute("user");
		try {
			List<Job> list;
			if (user != null && user.getRole().equals("manager")) {
				list = JobsDao.instance.getAll(ORSKEY, user.getShortKey().toString());
			} else if (user != null && user.getRole().equals("reviewer")) {
				list = JobsDao.instance.getAssignedJobs(ORSKEY, user.getShortKey(), user.getDepartment());
			} else {
				list = JobsDao.instance.getOpenJobs();
			}
			model.addAttribute("jobs", list);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "error";
		}
		return "jobs";
	}
	
	@RequestMapping(value="/jobs/{id}")
	public String visitJobPage(@PathVariable String id, ModelMap model) {
		try {
			Job j = JobsDao.instance.getById(id);
			model.addAttribute("job", j);
			return "jobDetails";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value="/advSearchJob", method={RequestMethod.GET})
	public String visitAdvSearchPage(ModelMap model) {
		model.addAttribute("statuses", RecruitmentStatus.values());
		model.addAttribute("teams", usersDao.getAllTeams());
		return "advSearchJob";
	}
	
	@RequestMapping(value="/advSearchJob", method={RequestMethod.POST})
	public String doAdvSearch(HttpServletRequest request, ModelMap model) {
		String closingDateFrom = request.getParameter("closingDateFrom");
		String closingDateTo = request.getParameter("closingDateTo");
		String salaryFrom = request.getParameter("salaryFrom");
		String salaryTo = request.getParameter("salaryTo");
		String positionType = request.getParameter("positionType");
		String location = request.getParameter("location");
		String description = request.getParameter("description");
		String status = request.getParameter("status");
		String assignedTeam = request.getParameter("assignedTeam");
		
		if (!validateSearchInput(
				closingDateFrom,
				closingDateTo,
				salaryFrom,
				salaryTo,
				positionType,
				location,
				description,
				status,
				assignedTeam)) {
			model.addAttribute("errorMsg", "Invalid form data");
			return "advSearchJob";
		}
		try {
			User user = (User) request.getSession().getAttribute("user");
			List<Job> list;
			if (user == null) {
				list = JobsDao.instance.search(
						null,
						null,
						closingDateFrom,
						closingDateTo,
						salaryFrom,
						salaryTo,
						positionType,
						location,
						description,
						status,
						assignedTeam);
			} else {
				list = JobsDao.instance.search(
						ORSKEY,
						user.getShortKey(),
						closingDateFrom,
						closingDateTo,
						salaryFrom,
						salaryTo,
						positionType,
						location,
						description,
						status,
						assignedTeam);
			}
			model.addAttribute("jobs", list);
			return "jobs";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping("/jobs/{id}/edit")
	public String visitEditJobPage(@PathVariable String id, HttpServletRequest request, ModelMap model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			model.addAttribute("errorMsg", "User has no permission");
			return "login";
		}
		try {
			Job j = JobsDao.instance.getById(id);
			model.addAttribute("job", j);
			model.addAttribute("statuses", RecruitmentStatus.values());
			model.addAttribute("teams", usersDao.getAllTeams());
			return "editJob";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value="/jobs/{id}/update", method={RequestMethod.POST})
	public String updateJob(@PathVariable String id, HttpServletRequest request, ModelMap model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			model.addAttribute("errorMsg", "User has no permission");
			return "login";
		}
		String closingDate = request.getParameter("closingDate");
		String salary = request.getParameter("salary");
		String positionType = request.getParameter("positionType");
		String location = request.getParameter("location");
		String description = request.getParameter("description");
		String status = request.getParameter("status");
		String assignedTeam = request.getParameter("assignedTeam");

		Job updatedJob = JobsDao.instance.getById(id);
		updatedJob.setClosingDate(closingDate);
		updatedJob.setSalary(Integer.parseInt(salary));
		updatedJob.setPositionType(positionType);
		updatedJob.setLocation(location);
		updatedJob.setDescription(description);
		updatedJob.setStatus(RecruitmentStatus.valueOf(status));
		if (assignedTeam.isEmpty()) {
			assignedTeam = null;
		}
		updatedJob.setAssignedTeam(assignedTeam);
		if (!validateInput(closingDate, salary, positionType, location, description, status, assignedTeam)) {
			model.addAttribute("errorMsg", "Invalid form data");
			model.addAttribute("job", updatedJob);
			return "addJob";
		}
		try {
			JobsDao.instance.update(ORSKEY, user.getShortKey(), updatedJob);
			return "redirect:/jobs/" + id;
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value="/jobs/{id}/delete")
	public String deleteJob(@PathVariable String id, HttpServletRequest request, ModelMap model) {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			model.addAttribute("errorMsg", "User has no permission");
			return "login";
		}
		try {
			JobsDao.instance.delete(ORSKEY, user.getShortKey(), id);
			return "redirect:/jobs";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "error";
		}
	}
	
	private boolean validateInput(
    		String closingDate,
    		String salary,
    		String positionType,
    		String location,
    		String description,
    		String status,
    		String assignedTeam) {
    	if (closingDate == null || closingDate.isEmpty()) {
    		return false;
    	}
    	try {
			Date d = DATE_FORMAT.parse(closingDate);
	    	if (!DATE_FORMAT.format(d).equals(closingDate)) {
	            return false;
	        }
		} catch (ParseException e) {
			return false;
		}
    	if (salary == null || salary.isEmpty()) {
    		return false;
    	}
    	try {
			Integer.parseInt(salary);
		} catch (NumberFormatException e) {
			return false;
		}
    	if (positionType == null || positionType.isEmpty()) {
    		return false;
    	}
    	if (location == null || location.isEmpty()) {
    		return false;
    	}
    	if (description == null) {
    		return false;
    	}
    	if (status != null) {
	    	try {
				RecruitmentStatus.valueOf(status);
			} catch (Exception e) {
				return false;
			}
    	}
    	if (assignedTeam != null && !usersDao.getAllTeams().contains(assignedTeam)) {
			return false;
    	}
    	return true;
    }
    
    private boolean validateSearchInput(
			String closingDateFrom,
			String closingDateTo,
			String salaryFrom,
			String salaryTo,
			String positionType,
			String location,
			String description,
			String status,
			String assignedTeam) {
    	if (closingDateFrom != null && !closingDateFrom.isEmpty()) {
        	try {
    			Date d = DATE_FORMAT.parse(closingDateFrom);
    	    	if (!DATE_FORMAT.format(d).equals(closingDateFrom)) {
    	            return false;
    	        }
    		} catch (ParseException e) {
    			return false;
    		}
    	}
    	if (closingDateTo != null && !closingDateTo.isEmpty()) {
        	try {
    			Date d = DATE_FORMAT.parse(closingDateTo);
    	    	if (!DATE_FORMAT.format(d).equals(closingDateTo)) {
    	            return false;
    	        }
    		} catch (ParseException e) {
    			return false;
    		}
    	}
    	if (salaryFrom != null && !salaryFrom.isEmpty()) {
        	try {
    			Integer.parseInt(salaryFrom);
    		} catch (NumberFormatException e) {
    			return false;
    		}
    	}
    	if (salaryTo != null && !salaryTo.isEmpty()) {
        	try {
    			Integer.parseInt(salaryTo);
    		} catch (NumberFormatException e) {
    			return false;
    		}
    	}
    	if (status != null && !status.isEmpty()) {
        	try {
    			RecruitmentStatus.valueOf(status);
    		} catch (Exception e) {
    			return false;
    		}
    	}
    	if (assignedTeam != null && !assignedTeam.isEmpty()
    			&& !usersDao.getAllTeams().contains(assignedTeam)) {
			return false;
    	}
    	return true;
    }
}