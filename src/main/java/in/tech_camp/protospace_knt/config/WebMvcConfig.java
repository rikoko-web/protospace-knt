// ファイルパス: src/main/java/in/tech_camp/protospace_knt/config/WebMvcConfig.java
package in.tech_camp.protospace_knt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // "/uploads/**" へのリクエストを、指定された絶対パスのフォルダへマッピングします
        // file: の後ろに絶対パスを指定することで、OSのどの場所からでも確実に画像を読み込めるようにします
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:/home/knt/java_projects/protospace-knt/uploads/");
    }
}