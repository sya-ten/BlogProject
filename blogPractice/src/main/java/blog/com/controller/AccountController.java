package blog.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import blog.com.BlogPracticeApplication;
import blog.com.model.dao.AccountDao;
import blog.com.model.entity.Account;
import jakarta.servlet.http.HttpSession;

@Controller
public class AccountController {
	@Autowired
	private AccountDao AccountDao;
	
	@GetMapping("/login")
	public String getLogin(HttpSession session,
						   Model model) {
		if (session.getAttribute("error") != null) {
			boolean error = (boolean)session.getAttribute("error");
			model.addAttribute("error", error);
			session.setAttribute("error", false);
		}
		
		return "login.html";
	}
	
	@PostMapping("/login")
	public String PostLogin(@RequestParam("email") String email,
							@RequestParam("password") String password,
							HttpSession session) {
		Account res = AccountDao.findByEmailAndPassword(email, password);
		if (res==null) {
			session.setAttribute("error", true);
			return "redirect:/login";
		}else {
			session.setAttribute("account", res);
			return "redirect:/blogList";
		}
	}
	
	@GetMapping("/register")
	public String getRegister(HttpSession session,
			   				  Model model) {
		if (session.getAttribute("error") != null) {
			boolean error = (boolean)session.getAttribute("error");
			model.addAttribute("error", error);
			session.setAttribute("error", false);
		}

		return "register.html";
	}
	
	@PostMapping("/register")
	public String PostRegister(@RequestParam("username") String username,
							   @RequestParam("email") String email,
							   @RequestParam("password") String password,
							   HttpSession session) {
		Account find = AccountDao.findByEmail(email);
		if (find!=null) {
			session.setAttribute("error", true);
			return "redirect:/register";
		}else {
			AccountDao.save(new Account(username, email, password));
			return "redirect:/login";
		}
	}
	
	
//	@PostMapping("/blogList")
//	public String PostblogList(@RequestParam("email") String email,
//							@RequestParam("password") String password) {
//		return "redirect:/register";
//	}
}
