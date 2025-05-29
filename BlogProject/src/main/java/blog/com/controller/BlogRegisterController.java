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
public class BlogRegisterController {
	// DAOを自動注入（DB操作用）
	@Autowired
	private BlogDao blogDao;
	// DAOを自動注入（DB操作用）
	@Autowired
	private AccountDao accountDao;
	
	// ブログ登録を表示
	@GetMapping("/blogRegister")
	public String getBlogRegister(HttpSession session) {
		//アカウント情報がないと、ログイン画面に進む
		Account account = (Account)session.getAttribute("account");
		if (account==null) {
			return "redirect:/login";
		}else {
			return "blogRegister.html";
		}
	}
	
	// ブログ登録処理
	@PostMapping("/blogRegister")
	public String blogRegister(@RequestParam("title") String title,
							   @RequestParam("img_path") MultipartFile img_path,
							   @RequestParam("content") String content,
							   HttpSession session) throws IOException {
		//アカウント情報がないと、ログイン画面に進む
		Account account = (Account)session.getAttribute("account");
		if (account==null) {
			return "redirect:/login";
		}
		//写真保存パスを設定する
		String uploadDir = "upload/";
		String fileName = System.currentTimeMillis() + "_" + img_path.getOriginalFilename();
		fileName = fileName.replace(" ", "");
		Path filePath = Paths.get(uploadDir, fileName);
		//写真を保存する
        Files.copy(img_path.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        String imgPath = "/upload/" + fileName;
        //ブログ情報をテーブルに保存する
		blogDao.save(new Blog(title, content, imgPath, account.getAccountId()));
		return "redirect:/blogList?rank=false";
	}
}
