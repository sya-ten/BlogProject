package blog.com.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import blog.com.model.entity.Account;
import blog.com.model.entity.Blog;
import blog.com.model.entity.Comment;
import jakarta.transaction.Transactional;

import java.util.List;


@Repository
public interface CommentDao extends JpaRepository<Comment, Long>{
	Comment save(Comment comment);
	List<Comment> findByBlogId(Long blogId);
}