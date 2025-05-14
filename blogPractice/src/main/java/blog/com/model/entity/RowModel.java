package blog.com.model.entity;

import java.sql.Timestamp;

public class RowModel {
	private Long blogId;
	private String title;
	private String content;
	private String createTm;
	private String author;
	
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
	public String getCreateTm() {
		return createTm;
	}
	public void setCreateTm(String createTm) {
		this.createTm = createTm;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}

	
	public RowModel() {
	}
	public RowModel(Long blogId, String title, String content, String createTm, String author) {
		this.blogId = blogId;
		this.title = title;
		this.content = content;
		this.createTm = createTm;
		this.author = author;
	}
}
