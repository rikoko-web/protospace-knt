package in.tech_camp.protospace_knt.config;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // application.properties から「uploads/」という値を取得します
    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 設定された「uploads/」を、実行しているPC環境に合わせた絶対パス（file:/...）に自動変換します
        String absolutePath = Paths.get(uploadPath).toAbsolutePath().toUri().toString();

        // "/uploads/**" へのアクセスを、自動計算したPC上の絶対パスへマッピングします
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(absolutePath);
    }
}