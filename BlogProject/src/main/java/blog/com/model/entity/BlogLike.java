package blog.com.model.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BlogLike {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long likeId;
	private Long likerId;
	private Long blogId;
	private Timestamp createTm;
	
	public Long getLikeId() {
		return likeId;
	}
	public void setLikeId(Long likeId) {
		this.likeId = likeId;
	}
	public Long getLikerId() {
		return likerId;
	}
	public void setLikerId(Long likerId) {
		this.likerId = likerId;
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
	
	public BlogLike() {
	}
	public BlogLike(Long likerId, Long blogId) {
		this.likerId = likerId;
		this.blogId = blogId;
		this.createTm = new Timestamp(System.currentTimeMillis());
	}
}

