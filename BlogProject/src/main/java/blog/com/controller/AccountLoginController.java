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
public class AccountLoginController {
	// DAOを自動注入（DB操作用）
	@Autowired
	private AccountDao AccountDao;
	
	// ログイン画面を表示
	@GetMapping("/login")
	public String getLogin(HttpSession session,
						   Model model) {
		// セッションにエラーがある場合、modelに渡して表示用
		if (session.getAttribute("error") != null) {
			boolean error = (boolean)session.getAttribute("error");
			model.addAttribute("error", error);
			// セッションにエラー初期化
			session.setAttribute("error", false);
		}
		return "login.html";
	}
	
	// ログイン処理
	@PostMapping("/login")
	public String PostLogin(@RequestParam("email") String email,
							@RequestParam("password") String password,
							HttpSession session,
							Model model) {
		Account res = AccountDao.findByEmailAndPassword(email, password);
		// ログイン失敗 → セッションにエラー保存してログイン画面にリダイレクト
		if (res==null) {
			session.setAttribute("error", true);
			return "redirect:/login";
		}
		// 成功 → ユーザー情報をセッションに保存してブログ一覧へ
		else {
			session.setAttribute("account", res);
			model.addAttribute("type", "list");
			return "redirect:/blogList";
		}
	}
}
