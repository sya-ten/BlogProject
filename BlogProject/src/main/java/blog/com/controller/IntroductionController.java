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
		//アカウント情報がないと、ログイン画面に進む
		Account account = (Account)session.getAttribute("account");
		if (account==null) {
			return "redirect:/login";
		}
		
		//編集モード
		if (type.equals("edit")) {
			model.addAttribute("edit", true);
		}
		//閲覧モード
		else {
			model.addAttribute("edit", false);
		}
		model.addAttribute("account", account);
		return "introduction.html";
	}
	
	// 自己紹介編集処理
	@PostMapping("/introduction/edit")
	public String PostLogin(@RequestParam("introduction") String introduction,
							HttpSession session,
							Model model) {
		//アカウント情報がないと、ログイン画面に進む
		Account account = (Account)session.getAttribute("account");
		if (account==null) {
			return "redirect:/login";
		}
		
		account.setIntroduction(introduction);
		//アカウント情報をテーブルに保存する
		AccountDao.save(account);
		
		model.addAttribute("account", account);
		model.addAttribute("edit", false);
		return "redirect:/introduction?type=view";
	}
}
