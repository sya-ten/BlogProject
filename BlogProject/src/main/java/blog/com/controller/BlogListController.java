package blog.com.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import blog.com.model.dao.AccountDao;
import blog.com.model.dao.BlogDao;
import blog.com.model.entity.Account;
import blog.com.model.entity.Blog;
import blog.com.model.entity.BlogModel;
import blog.com.model.entity.RowModel;
import jakarta.servlet.http.HttpSession;

@Controller
public class BlogListController {
	// DAOを自動注入（DB操作用）
	@Autowired
	private BlogDao blogDao;
	// DAOを自動注入（DB操作用）
	@Autowired
	private AccountDao accountDao;

	// ブログ一覧を表示
	@GetMapping("/blogList")
	public String getBlogList(@RequestParam("rank") Boolean rank,
							  HttpSession session,
							  Model model) {
		//アカウント情報がないと、ログイン画面に進む
		Account account = (Account)session.getAttribute("account");
		if (account==null) {
			return "redirect:/login";
		}else {
			model.addAttribute("rank", rank);
			return "blogList.html";
		}
	}
	
	// ブログ検索結果を表示
	@GetMapping("/search")
	public String getBlogSearch(@RequestParam("rank") Boolean rank,
								@RequestParam("title") String title,
							    HttpSession session,
								Model model) {
		//アカウント情報がないと、ログイン画面に進む
		Account account = (Account)session.getAttribute("account");
		if (account==null) {
			return "redirect:/login";
		}else {
			model.addAttribute("rank", rank);
			model.addAttribute("title", title);
			return "blogList.html";
		}
	}
	
	
	// ブログ一覧初期化
	@GetMapping("/blogList/data")
	@ResponseBody
	public Object getBlogListData(@RequestParam("rank") Boolean rank,
								  @RequestParam("title") String title,
								  HttpSession session) {
		//アカウント情報がないと、ログイン画面に進む
		Account account = (Account)session.getAttribute("account");
		if (account==null) {
			return "redirect:/login";
		}
		
		List<Blog> all;
		if (title.equals("null")) {
			all = blogDao.findAll();
		}else {
			all = blogDao.findByTitleContaining(title);
		}
		
		all.sort((a,b)->b.getCreateTm().compareTo(a.getCreateTm()));
		
		if (!rank.equals("null")&&rank==true) {
			all.sort((a,b)->Integer.compare(b.getViewTimes(), a.getViewTimes()));
		}
		
		
		ArrayList<RowModel> arr = new ArrayList<RowModel>();
		
		for (Blog blog : all) {
			String username = accountDao.findByAccountId(blog.getAuthorId()).getUsername();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDateTime createTm = blog.getCreateTm().toLocalDateTime();
			String createTmStr = createTm.format(formatter);
			
			arr.add(new RowModel(blog.getBlogId(), blog.getTitle(), blog.getContent(), blog.getViewTimes(), createTmStr, username));
		}
		return arr;
	}
}
