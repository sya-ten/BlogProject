package blog.com.model.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Blog {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long blogId;
	private String title;
	private String content;
	private String imgPath;
	private Timestamp createTm;
	private Timestamp updateTm;
	private Long authorId;
	
	public Long getBlogId() {
		return blogId;
	}
	public void setBlogId(Long blogId) {
		this.blogId = blogId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public Timestamp getCreateTm() {
		return createTm;
	}
	public void setCreateTm(Timestamp createTm) {
		this.createTm = createTm;
	}
	public Timestamp getUpdateTm() {
		return updateTm;
	}
	public void setUpdateTm(Timestamp updateTm) {
		this.updateTm = updateTm;
	}
	public Long getAuthorId() {
		return authorId;
	}
	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}
	
	public Blog() {
	}
	public Blog(String title, String content, String imgPath, 
			Long authorId) {
		this.title = title;
		this.content = content;
		this.imgPath = imgPath;
		this.createTm = new Timestamp(System.currentTimeMillis());
		this.updateTm = null;
		this.authorId = authorId;
	}
}

