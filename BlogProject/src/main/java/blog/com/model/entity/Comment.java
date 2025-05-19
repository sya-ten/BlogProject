package blog.com.model.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long commentId;
	private String content;
	private Long commenterId;
	private Long blogId;
	private Timestamp createTm;
	
	public Long getCommentId() {
		return commentId;
	}
	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getCommenterId() {
		return commenterId;
	}
	public void setCommenterId(Long commenterId) {
		this.commenterId = commenterId;
	}
	public Long getBlogId() {
		return blogId;
	}
	public void setBlogId(Long blogId) {
		this.blogId = blogId;
	}
	public Timestamp getCreateTm() {
		return createTm;
	}
	public void setCreateTm(Timestamp createTm) {
		this.createTm = createTm;
	}
	
	public Comment() {
	}
	public Comment(String content, Long commenterId, Long blogId) {
		this.content = content;
		this.commenterId = commenterId;
		this.blogId = blogId;
		this.createTm = new Timestamp(System.currentTimeMillis());
	}
	
	
}

