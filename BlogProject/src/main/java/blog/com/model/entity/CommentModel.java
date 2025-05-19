package blog.com.model.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class CommentModel {
	private Long commentId;
	private String content;
	private String commenterName;
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
	public String getCommenterName() {
		return commenterName;
	}
	public void setCommenterName(String commenterName) {
		this.commenterName = commenterName;
	}
	public Timestamp getCreateTm() {
		return createTm;
	}
	public void setCreateTm(Timestamp createTm) {
		this.createTm = createTm;
	}
	
	public CommentModel() {
	}
	public CommentModel(Long commentId, String content, String commenterName, Timestamp createTm) {
		this.commentId = commentId;
		this.content = content;
		this.commenterName = commenterName;
		this.createTm = createTm;
	}
}

