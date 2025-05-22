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
import blog.com.model.dao.BlogLikeDao;
import blog.com.model.dao.CommentDao;
import blog.com.model.entity.Account;
import blog.com.model.entity.Blog;
import blog.com.model.entity.BlogLike;
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
	// DAOを自動注入（DB操作用）
	@Autowired
	private BlogLikeDao blogLikeDao;
	
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
		
		//ブログを取得する
		Blog blog = blogDao.findByBlogId(blogId);
		//登録したアカウントはこのブログの作者と同じ人の場合、ブログ編集ボタンを見えるようにする
		if (account.getAccountId().equals(blog.getAuthorId())) {
			model.addAttribute("editable", true);
		} 
		//閲覧回数+1、保存
		blog.setViewTimes(blog.getViewTimes()+1);
		blogDao.save(blog);
		session.setAttribute("blog", blog);
		//ブログ作成日をテーブル様式から変換する
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime createTm = blog.getCreateTm().toLocalDateTime();
		String createTmStr = createTm.format(formatter);
		//ブログ更新日をテーブル様式から変換する
		String updateTmStr = "";
		if (blog.getUpdateTm()!=null) {
			LocalDateTime updateTm = blog.getUpdateTm().toLocalDateTime();
			updateTmStr = updateTm.format(formatter);
		}
		//ブログ内容を改行する
		String content = blog.getContent();
		content = content.replace("\r\n", "<br/>");
		//ブログviewModelに変換する
		BlogModel blogModel = new BlogModel(blogId, blog.getTitle(), content, blog.getImgPath(), blog.getViewTimes(), createTmStr, updateTmStr);
		account = accountDao.findByAccountId(blog.getAuthorId());
		
		model.addAttribute("blog", blogModel);
		model.addAttribute("account", account);
		//コメントListを取得する
		List<Comment> comments = commentDao.findByBlogId(blogId);
		//コメントviewModelに変換する
		ArrayList<CommentModel> commentModels = new ArrayList<CommentModel>();
		for (Comment comment : comments) {
			Account commenter = accountDao.findByAccountId(comment.getCommenterId());
			String commenterName = commenter.getUsername();
			commentModels.add(new CommentModel(comment.getCommentId(),comment.getContent(),commenterName,comment.getCreateTm()));
		}
			
		model.addAttribute("comments", commentModels);
		//いいね情報を取得する
		List<BlogLike> res = blogLikeDao.findByBlogIdAndLikerId(blogId, account.getAccountId());
		if (res.size()==0) {
			model.addAttribute("blogLike", false);
		}else {
			model.addAttribute("blogLike", true);
		}
		
	    return "blog.html";
	}
}
