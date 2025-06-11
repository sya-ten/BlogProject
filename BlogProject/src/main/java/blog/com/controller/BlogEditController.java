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
import org.springframework.web.multipart.MultipartFile;

import blog.com.model.dao.AccountDao;
import blog.com.model.dao.BlogDao;
import blog.com.model.entity.Account;
import blog.com.model.entity.Blog;
import blog.com.model.entity.BlogModel;
import jakarta.servlet.http.HttpSession;

@Controller
public class BlogEditController {
	// DAOを自動注入（DB操作用）
	@Autowired
	private BlogDao blogDao;
	// DAOを自動注入（DB操作用）
	@Autowired
	private AccountDao AccountDao;

	// ブログ編集画面を表示
	@GetMapping("/blogEdit")
	public String getBlogEdit(@RequestParam("id") Long id,
							  HttpSession session,
						   	  Model model) {
		//アカウント情報がないと、ログイン画面に進む
		Account account = (Account)session.getAttribute("account");
		if (account==null) {
			return "redirect:/login";
		}
		
		//ブログを取得する
		Blog blog = blogDao.findByBlogId(id);
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
		//ブログviewModelに変換する
		BlogModel blogModel = new BlogModel(id, blog.getTitle(), blog.getContent(), blog.getImgPath(), blog.getViewTimes(), createTmStr, updateTmStr);
		model.addAttribute("blog", blogModel);
		
		return "blogEdit.html";
	}
	
	// ブログ編集処理
	@PostMapping("/blogEdit")
	public String postBlogEdit(@RequestParam("action") String action,
							   @RequestParam("title") String title,
							   @RequestParam("img_path") MultipartFile img_path,
							   @RequestParam("content") String content,
							   HttpSession session) throws IOException {
		//アカウント情報がないと、ログイン画面に進む
		Account account = (Account)session.getAttribute("account");
		if (account==null) {
			return "redirect:/login";
		}
		
		//ブログを取得する
		Blog blog = (Blog)session.getAttribute("blog");
		//登録したアカウントはこのブログの作者と違う人の場合、ログイン画面に進む
		Long blogId = blog.getBlogId();
		if (!blog.getAuthorId().equals(account.getAccountId())) {
			//sessionをクリアする
			session.invalidate();
			return "redirect:/login";
		}
		
		// 更新処理
		if ("update".equals(action)) {
			//写真保存パスを設定する
			String uploadDir = "src/main/resources/static/upload/";
			String fileName = System.currentTimeMillis() + "_" + img_path.getOriginalFilename();
			fileName = fileName.replace(" ", "");
			Path filePath = Paths.get(uploadDir, fileName);
			//写真を保存する
	        Files.copy(img_path.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
	        String imgPath = "/upload/" + fileName;
	        //ブログ情報を変更する
			blog.setTitle(title);
			if (!img_path.getOriginalFilename().equals("")) {
				blog.setImgPath(imgPath);
			}
			blog.setContent(content);
			blog.setUpdateTm(new Timestamp(System.currentTimeMillis()));
			//ブログ情報をテーブルに更新する
	        blogDao.save(blog);
	    } 
		//　削除処理
		else if ("delete".equals(action)) {
			//テーブルに削除する
			blogDao.deleteByBlogId(blogId);
			//sessionにブログ情報をクリアする
			session.removeAttribute("blog");
	    }
        return "redirect:/blogList";
	}
}
