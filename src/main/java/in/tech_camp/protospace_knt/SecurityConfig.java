package in.tech_camp.protospace_knt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // ★これの追加が必要です
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // 💡 新規登録（/signUp）やログイン画面、画像などは未ログインでもアクセスOK
                .requestMatchers("/", "/login", "/signUp", "/css/**", "/images/**").permitAll() 
                
                // ★追加：プロトタイプ詳細やユーザー詳細（マイページ）の「見るだけ（GET）」なら未ログインでもアクセスOKにする
                .requestMatchers(HttpMethod.GET, "/prototypes/**", "/users/**").permitAll() 
                
                .anyRequest().authenticated() // それ以外（マイページへの遷移中継や投稿など）はログイン必須
            )
            .formLogin(form -> form
                .loginPage("/login") // 自作ログイン画面のURL
                .loginProcessingUrl("/login") // フォームの送信先URL
                .usernameParameter("email")   // ログインIDに「email」を使う
                .passwordParameter("password") // パスワードは「password」
                .defaultSuccessUrl("/", true) // ログイン成功後はトップページへ
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/") // ログアウト後はトップページへ
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}