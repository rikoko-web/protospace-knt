// ファイルパス: src/main/java/in/tech_camp/protospace_knt/config/WebMvcConfig.java
package in.tech_camp.protospace_knt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // "/uploads/**" へのリクエストをプロジェクトルートの "uploads/" フォルダへマッピング
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}