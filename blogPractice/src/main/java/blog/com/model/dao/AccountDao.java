package blog.com.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import blog.com.model.entity.Account;
import java.util.List;


@Repository
public interface AccountDao extends JpaRepository<Account, Long>{
	Account save(Account account);
	Account findByAccountId(Long accountId);
	Account findByEmail(String email);
	Account findByEmailAndPassword(String email, String password);
}