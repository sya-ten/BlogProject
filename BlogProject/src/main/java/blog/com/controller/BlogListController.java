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
	public String getBlogList(HttpSession session,
							 Model model) {
		Account account = (Account)session.getAttribute("account");
		if (account==null) {
			return "redirect:/login";
		}else {
			return "blogList.html";
		}
	}
	
	// ブログ検索結果を表示
	@GetMapping("/search")
	public String getBlogSearch(HttpSession session,
								@RequestParam("title") String title,
								Model model) {
		Account account = (Account)session.getAttribute("account");
		if (account==null) {
			return "redirect:/login";
		}else {
			model.addAttribute("title", title);
			return "blogList.html";
		}
	}
	
	
	// ブログ一覧初期化
	@GetMapping("/blogList/data")
	@ResponseBody
	public Object getBlogListData(@RequestParam("title") String title,
								  HttpSession session) {
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
		ArrayList<RowModel> arr = new ArrayList<RowModel>();
		
		for (Blog blog : all) {
			String username = accountDao.findByAccountId(blog.getAuthorId()).getUsername();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDateTime createTm = blog.getCreateTm().toLocalDateTime();
			String createTmStr = createTm.format(formatter);
			
			arr.add(new RowModel(blog.getBlogId(), blog.getTitle(), blog.getContent(), createTmStr, username));
		}
		return arr;
	}
}
