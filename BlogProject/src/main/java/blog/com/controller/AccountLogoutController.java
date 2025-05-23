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
public class AccountLogoutController {
	// DAOを自動注入（DB操作用）
	@Autowired
	private AccountDao AccountDao;
	
	// ログアウト処理
	@GetMapping("/logout")
	public String getLogout(HttpSession session) {
		// sessionをクリアする
		session.invalidate();
		return "login.html";
	}
}
