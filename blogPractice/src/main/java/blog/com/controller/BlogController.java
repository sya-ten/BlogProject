package blog.com.controller;

import java.io.File;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import blog.com.BlogPracticeApplication;
import blog.com.model.dao.AccountDao;
import blog.com.model.dao.BlogDao;
import blog.com.model.entity.Account;
import blog.com.model.entity.Blog;
import blog.com.model.entity.BlogModel;
import blog.com.model.entity.RowModel;
import jakarta.servlet.http.HttpSession;

@Controller
public class BlogController {
	private static final String Account = null;
	@Autowired
	private BlogDao blogDao;
	@Autowired
	private AccountDao accountDao;

	@GetMapping("/blogList")
	public String getBlogList(HttpSession session) {
		Account account = (Account)session.getAttribute("account");
		if (account==null) {
			return "redirect:/login";
		}
		return "blogList.html";
	}
	
	@GetMapping("/blogList/data")
	@ResponseBody
	public Object getBlogListData(HttpSession session) {
		Account account = (Account)session.getAttribute("account");
		if (account==null) {
			return "redirect:/login";
		}
		
		List<Blog> all = blogDao.findAll();
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
	
	@GetMapping("/blog/{blogId}")
	public String getBlog(@PathVariable("blogId") Long blogId, Model model, HttpSession session) {
		Account account = (Account)session.getAttribute("account");
		if (account==null) {
			return "redirect:/login";
		}
		
		Blog blog = blogDao.findByBlogId(blogId);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime createTm = blog.getCreateTm().toLocalDateTime();
		String createTmStr = createTm.format(formatter);
		String updateTmStr = "";
		if (blog.getUpdateTm()!=null) {
			LocalDateTime updateTm = blog.getUpdateTm().toLocalDateTime();
			updateTmStr = updateTm.format(formatter);
		}
		BlogModel blogModel = new BlogModel(blogId, blog.getTitle(), blog.getContent(), blog.getImgPath(), createTmStr, updateTmStr);
		account = accountDao.findByAccountId(blog.getAuthorId());
		
		model.addAttribute("blog", blogModel);
		model.addAttribute("account", account);
	    return "blog.html";
	}

	@GetMapping("/blogRegister")
	public String getBlogRegister() {
		return "blogRegister.html";
	}
	
	@PostMapping("/blogRegister")
	public String blogRegister(@RequestParam("title") String title,
							   @RequestParam("img_path") MultipartFile img_path,
							   @RequestParam("content") String content,
							   HttpSession session) throws IOException {
		Account account = (Account) session.getAttribute("account");
		String uploadDir = "src/main/resources/static/upload/";
		String fileName = System.currentTimeMillis() + "_" + img_path.getOriginalFilename();
		fileName = fileName.replace(" ", "");
		Path filePath = Paths.get(uploadDir, fileName);
        Files.copy(img_path.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        String imgPath = "/upload/" + fileName;
		blogDao.save(new Blog(title, content, imgPath, null, account.getAccountId()));
		return "redirect:/blogList";
	}
}
