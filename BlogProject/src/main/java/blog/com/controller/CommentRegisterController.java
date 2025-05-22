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
public class CommentRegisterController {
	// DAOを自動注入（DB操作用）
	@Autowired
	private BlogDao blogDao;
	// DAOを自動注入（DB操作用）
	@Autowired
	private AccountDao accountDao;
	// DAOを自動注入（DB操作用）
	@Autowired
	private CommentDao commentDao;
	
	// ブログ登録処理
	@PostMapping("/commentRegister")
	public String commentRegister(@RequestParam("content") String content,
								  @RequestParam("blogId") Long blogId,
							      HttpSession session) throws IOException {
		//アカウント情報がないと、ログイン画面に進む
		Account account = (Account)session.getAttribute("account");
		if (account==null) {
			return "redirect:/login";
		}
		
		Comment comment = new Comment(content, account.getAccountId(), blogId);
		//コメント情報をテーブルに保存する
		commentDao.save(comment);
		return "redirect:/blog/" + blogId;
	}
}
