package blog.com.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import blog.com.model.entity.Account;
import blog.com.model.entity.Blog;
import blog.com.model.entity.BlogLike;
import jakarta.transaction.Transactional;

import java.util.List;


@Repository
public interface BlogLikeDao extends JpaRepository<BlogLike, Long>{
	BlogLike save(BlogLike blogLike);
	
	@Transactional
	void deleteByLikeId(Long likeId);
}