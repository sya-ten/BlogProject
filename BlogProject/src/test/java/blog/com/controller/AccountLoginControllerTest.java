package blog.com.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import blog.com.model.dao.AccountDao;
import blog.com.model.entity.Account;
import jakarta.servlet.http.HttpSession;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountLoginControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private AccountDao accountDao;
	MockHttpSession session = new MockHttpSession();
	
	@BeforeEach
	public void prepareData() {
		// ログインが成功：　email "alice@123.com"、　password "123"　true
		when(accountDao.findByEmailAndPassword("alice@123.com", "123")).thenReturn(new Account("Alice", "alice@123.com", "123"));
		// ログインが失敗：　email "ana@123.com"と等しい、　パスワードはどんな値でもいい　false
		when(accountDao.findByEmailAndPassword(eq("ana@123.com"), any())).thenReturn(null);
		// ログインが失敗：　email ""と等しい、　パスワードは""と等しい　false
		when(accountDao.findByEmailAndPassword("","")).thenReturn(null);
	}
	
	// ログインページを正しく取得するテスト
	@Test
	public void testLoginPage_success() throws Exception {
		mockMvc.perform(get("/login").sessionAttr("error", true))
	       .andExpect(model().attribute("error", true))
	       .andExpect(view().name("login.html"));
	}
		
	// ログインが成功した場合のテスト
	@Test
	public void testLogin_success() throws Exception {
		mockMvc.perform(post("/login")
		        .param("email", "alice@123.com")
		        .param("password", "123")
		        .session(session))
		    .andExpect(redirectedUrl("/blogList?rank=false"));

		// セッションに保存されたAccountの検証
		Object accountAttr = session.getAttribute("account");
		assertNotNull(accountAttr);
		assertTrue(accountAttr instanceof Account);
		assertEquals("Alice", ((Account) accountAttr).getUsername());
		assertEquals("alice@123.com", ((Account) accountAttr).getEmail());
	}
	
	// ログインが失敗した場合のテスト
		@Test
		public void testLogin_fail() throws Exception {
			mockMvc.perform(post("/login")
			        .param("email", "ana@123.com")
			        .param("password", "123")
			        .session(session))
			    .andExpect(redirectedUrl("/login"));

			// セッションに保存されたAccountの検証
			Object error = session.getAttribute("error");
			assertNotNull(error);
			assertEquals(true, error);
		}
		
		// ログインが失敗した場合のテスト
		@Test
		public void testLogin_fail_withNonStr() throws Exception {
			mockMvc.perform(post("/login")
			        .param("email", "")
			        .param("password", "")
			        .session(session))
			    .andExpect(redirectedUrl("/login"));

			// セッションに保存されたAccountの検証
			Object error = session.getAttribute("error");
			assertNotNull(error);
			assertEquals(true, error);
		}
}
