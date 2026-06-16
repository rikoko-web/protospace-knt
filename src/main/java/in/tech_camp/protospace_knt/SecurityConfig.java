package in.tech_camp.protospace_knt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                .requestMatchers("/", "/login", "/signup", "/css/**", "/images/**").permitAll() // ログイン不要なページ
                .anyRequest().authenticated() // それ以外はログイン必須
            )
            .formLogin(form -> form
                .loginPage("/login") // 自作ログイン画面のURL
                .loginProcessingUrl("/login") // 💡 フォームの送信先URL
                .usernameParameter("email")   // 💡 ログインIDに「email」を使う
                .passwordParameter("password") // 💡 パスワードは「password」
                .defaultSuccessUrl("/", true) // ログイン成功後の飛び先
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/") // ログアウト後の飛び先
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}