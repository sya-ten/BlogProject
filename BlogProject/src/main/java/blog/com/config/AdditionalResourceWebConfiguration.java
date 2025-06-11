package blog.com.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Springの設定をするクラスだと宣言
@Configuration
public class AdditionalResourceWebConfiguration implements WebMvcConfigurer{
													//Spring MVCの設定をカスタマイズ
	@Override
	// addResourceHandlers(ResourceHandlerRegistry registry): 
	// このメソッドは、静的リソースのリソースハンドラーを追加するために使用
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/upload/**").addResourceLocations("file:./upload/");

    }
	
}