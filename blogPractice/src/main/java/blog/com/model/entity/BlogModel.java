package blog.com.model.entity;

public class BlogModel {
	private Long blogId;
	private String title;
	private String content;
	private String imgPath;
	private String createTm;
	private String updateTm;
	
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
	public String getCreateTm() {
		return createTm;
	}
	public void setCreateTm(String createTm) {
		this.createTm = createTm;
	}
	public String getUpdateTm() {
		return updateTm;
	}
	public void setUpdateTm(String updateTm) {
		this.updateTm = updateTm;
	}
	
	public BlogModel() {
	}
	public BlogModel(Long blogId, String title, String content, String imgPath, String createTm, String updateTm) {
		this.blogId = blogId;
		this.title = title;
		this.content = content;
		this.imgPath = imgPath;
		this.createTm = createTm;
		this.updateTm = updateTm;
	}
	
}

