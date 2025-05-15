package blog.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import blog.com.model.dao.AccountDao;
import blog.com.model.entity.Account;
import jakarta.servlet.http.HttpSession;

@Controller
public class AccountRegisterController {
	// DAOを自動注入（DB操作用）
	@Autowired
	private AccountDao AccountDao;
	
	// 登録画面を表示
	@GetMapping("/register")
	public String getRegister(HttpSession session,
			   				  Model model) {
		// セッションにエラーがある場合、modelに渡して表示用
		if (session.getAttribute("error") != null) {
			boolean error = (boolean)session.getAttribute("error");
			model.addAttribute("error", error);
			// セッションにエラー初期化
			session.setAttribute("error", false);
		}
		return "register.html";
	}
	
	// 登録処理
	@PostMapping("/register")
	public String PostRegister(@RequestParam("username") String username,
							   @RequestParam("email") String email,
							   @RequestParam("password") String password,
							   HttpSession session) {
		Account find = AccountDao.findByEmail(email);
		// 当メールはすでに存在 → エラー表示して登録画面にリダイレクト
		if (find!=null) {
			session.setAttribute("error", true);
			return "redirect:/register";
		}
		// 当メールは存在しない → 保存処理してログイン画面にリダイレクト
		else {
			AccountDao.save(new Account(username, email, password));
			return "redirect:/login";
		}
	}
}
