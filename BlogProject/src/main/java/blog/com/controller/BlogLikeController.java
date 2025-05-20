package blog.com.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import blog.com.model.dao.AccountDao;
import blog.com.model.dao.BlogDao;
import blog.com.model.dao.BlogLikeDao;
import blog.com.model.entity.Account;
import blog.com.model.entity.Blog;
import blog.com.model.entity.BlogLike;
import blog.com.model.entity.BlogModel;
import jakarta.servlet.http.HttpSession;

@Controller
public class BlogLikeController {
	// DAOを自動注入（DB操作用）
	@Autowired
	private BlogDao blogDao;
	// DAOを自動注入（DB操作用）
	@Autowired
	private AccountDao AccountDao;
	// DAOを自動注入（DB操作用）
	@Autowired
	private BlogLikeDao blogLikeDao;

	// ブログ編集画面を表示
	@PostMapping("/blogLike")
	@ResponseBody
	public void PostBlogLike(@RequestParam("liker_id") Long liker_id,
							 @RequestParam("blog_id") Long blog_id,
							 @RequestParam("add") Boolean add,
							 HttpSession session,
						   	 Model model) {
		BlogLike blogLike = new BlogLike(liker_id, blog_id);
		//いいね追加モード
		if (add) {
			blogLikeDao.save(blogLike);
		}
		//いいね削除モード
		else {
			blogLikeDao.deleteByBlogIdAndLikerId(blog_id, liker_id);
		}
	}
}
