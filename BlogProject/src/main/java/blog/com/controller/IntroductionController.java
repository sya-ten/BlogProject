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
public class IntroductionController {
	// DAOを自動注入（DB操作用）
	@Autowired
	private AccountDao AccountDao;
	
	// 自己紹介画面を表示
	@GetMapping("/introduction")
	public String getIntroduction(@RequestParam("type") String type,
								  HttpSession session,
						   		  Model model) {
		Account account = (Account)session.getAttribute("account");
		if (account==null) {
			return "redirect:/login";
		}
		
		if (type.equals("edit")) {
			model.addAttribute("edit", true);
		}else {
			model.addAttribute("edit", false);
		}
		
		model.addAttribute("account", account);
		return "introduction.html";
	}
//	
//	// 自己紹介編集画面を表示
//	@GetMapping("//introduction/edit")
//	public String getIntroductionEdit(HttpSession session,
//						   		  	  Model model) {
//		Account account = (Account)session.getAttribute("account");
//		if (account==null) {
//			return "redirect:/login";
//		}
//		model.addAttribute("account", account);
//		return "introduction.html";
//	}
	
//	// ログイン処理
//	@PostMapping("/login")
//	public String PostLogin(@RequestParam("email") String email,
//							@RequestParam("password") String password,
//							HttpSession session,
//							Model model) {
//		Account res = AccountDao.findByEmailAndPassword(email, password);
//		// ログイン失敗 → セッションにエラー保存してログイン画面にリダイレクト
//		if (res==null) {
//			session.setAttribute("error", true);
//			return "redirect:/login";
//		}
//		// 成功 → ユーザー情報をセッションに保存してブログ一覧へ
//		else {
//			session.setAttribute("account", res);
//			model.addAttribute("type", "list");
//			return "redirect:/blogList";
//		}
//	}
}
