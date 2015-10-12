package au.edu.unsw.soacourse.ors.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import au.edu.unsw.soacourse.ors.beans.*;
import au.edu.unsw.soacourse.ors.dao.UsersDao;

@Controller
public class AuthenticationController {

	@RequestMapping("/login")
	public String visitLoginPage(ModelMap model) {
		return "login";
	}
	
	@RequestMapping("/doLogin")
	public String doLogin(HttpServletRequest request, ModelMap model) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		if (username == null || username.isEmpty()
			|| password == null || password.isEmpty()) {
			model.addAttribute("errorMsg", "Username and password cannot be empty");
			return "login";
		}
		try {
			User user = UsersDao.instance.login(username, password);
			if (user == null) {
				model.addAttribute("errorMsg", "Login failed");
				return "login";
			}
			request.getSession().setAttribute("user", user);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "error";
		}
		return "index";
	}
	
	@RequestMapping("/logout")
	public String doLogout(HttpServletRequest request, ModelMap model) {
		request.getSession().invalidate();
		return "index";
	}
	
//	@RequestMapping("/submitRequest")
//	public String submitLoanRequest(@RequestParam Map<String, String> allRequestParams, ModelMap model) {
//		LoanInputType loanreq = new LoanInputType();
//		loanreq.setFirstName(allRequestParams.get("firstName"));
//		loanreq.setName(allRequestParams.get("lastName"));
//		loanreq.setAmount(new BigInteger(allRequestParams.get("amount")));
//		ApprovalType response = loanApproval.approve(loanreq);
//		model.addAttribute("result", response.getAccept());
//		return "loanRequestResult";
//	}
}