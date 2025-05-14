package blog.com.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import blog.com.model.entity.Account;
import blog.com.model.entity.Blog;

import java.util.List;


@Repository
public interface BlogDao extends JpaRepository<Blog, Long>{
	List<Blog> findAll();
	Blog findByBlogId(Long blogId);
}