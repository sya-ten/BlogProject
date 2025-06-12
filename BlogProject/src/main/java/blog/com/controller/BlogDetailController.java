package blog.com.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import blog.com.model.dao.CommentDao;
import blog.com.model.entity.Account;
import blog.com.model.entity.Blog;
import blog.com.model.entity.BlogModel;
import blog.com.model.entity.Comment;
import blog.com.model.entity.CommentModel;
import blog.com.model.entity.RowModel;
import jakarta.servlet.http.HttpSession;

@Controller
public class BlogDetailController {
	// DAOを自動注入（DB操作用）
	@Autowired
	private BlogDao blogDao;
	// DAOを自動注入（DB操作用）
	@Autowired
	private AccountDao accountDao;
	// DAOを自動注入（DB操作用）
	@Autowired
	private CommentDao commentDao;
	
	// ブログ詳細を表示
	@GetMapping("/blog/{blogId}")
	public String getBlog(@PathVariable("blogId") Long blogId, 
						  Model model, 
						  HttpSession session) {
		//アカウント情報がないと、ログイン画面に進む
		Account account = (Account)session.getAttribute("account");
		if (account==null) {
			return "redirect:/login";
		}
		
		Blog blog = blogDao.findByBlogId(blogId);
		if (account.getAccountId().equals(blog.getAuthorId())) {
			model.addAttribute("editable", true);
		} 
		blog.setViewTimes(blog.getViewTimes()+1);
		blogDao.save(blog);
		session.setAttribute("blog", blog);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime createTm = blog.getCreateTm().toLocalDateTime();
		String createTmStr = createTm.format(formatter);
		String updateTmStr = "";
		if (blog.getUpdateTm()!=null) {
			LocalDateTime updateTm = blog.getUpdateTm().toLocalDateTime();
			updateTmStr = updateTm.format(formatter);
		}
		BlogModel blogModel = new BlogModel(blogId, blog.getTitle(), blog.getContent(), blog.getImgPath(), blog.getViewTimes(), createTmStr, updateTmStr);
		account = accountDao.findByAccountId(blog.getAuthorId());
		
		model.addAttribute("blog", blogModel);
		model.addAttribute("account", account);
		
		List<Comment> comments = commentDao.findByBlogId(blogId);
		ArrayList<CommentModel> commentModels = new ArrayList<CommentModel>();
		for (Comment comment : comments) {
			Account commenter = accountDao.findByAccountId(comment.getCommenterId());
			String commenterName = commenter.getUsername();
			commentModels.add(new CommentModel(comment.getCommentId(),comment.getContent(),commenterName,commenter.getCreateTm()));
		}
			
		model.addAttribute("comments", commentModels);
		
	    return "blog.html";
	}
}
