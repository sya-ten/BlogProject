package blog.com.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
public class AccountRegisterControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private AccountDao accountDao;
	MockHttpSession session = new MockHttpSession();
	
	@BeforeEach
	public void prepareData() {
		// 登録が成功：　email "alice@123.com"
		when(accountDao.findByEmail("alice@123.com")).thenReturn(null);
		when(accountDao.save(new Account("ALice", "alice@123.com", "123"))).thenReturn(new Account("Alice", "alice@123.com", "123"));
		// 登録が失敗：　email "ana@123.com"
		when(accountDao.findByEmail("ana@123.com")).thenReturn(new Account("Ana", "ana@123.com", "123"));
	}
	
	// 登録ページを正しく取得するテスト
	@Test
	public void testRegisterPage_success() throws Exception {
		mockMvc.perform(get("/register").sessionAttr("error", true))
	       .andExpect(model().attribute("error", true))
	       .andExpect(view().name("register.html"));
	}
		
	// 登録が成功した場合のテスト
	@Test
	public void testRegister_success() throws Exception {
		mockMvc.perform(post("/register")
				.param("username", "Alice")
		        .param("email", "alice@123.com")
		        .param("password", "123")
		        .session(session))
		    .andExpect(redirectedUrl("/login"));
		
		// save() は１回呼び出されることを確認
		verify(accountDao, times(1)).save(any());
		}
	
	// 登録が失敗した場合のテスト
		@Test
		public void testRegister_fail() throws Exception {
			mockMvc.perform(post("/register")
					.param("username", "Ana")
			        .param("email", "ana@123.com")
			        .param("password", "123")
			        .session(session))
			    .andExpect(redirectedUrl("/register"));

			// セッションに保存されたAccountの検証
			Object error = session.getAttribute("error");
			assertNotNull(error);
			assertEquals(true, error);
			
			// save() は一度も呼ばれていないことを確認
			verify(accountDao, times(0)).save(any());
		}
}
