package in.tech_camp.protospace_knt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                // 1. 公開ページ（未ログインでもOK）
                .requestMatchers("/", "/login", "/signUp", "/css/**", "/images/**", "/uploads/**").permitAll() 
                
                // 2. プロトタイプの閲覧（GETのみOK）
                .requestMatchers(HttpMethod.GET, "/prototypes/**", "/users/**").permitAll() 
                
                // 3. その他すべてはログイン必須
                .anyRequest().authenticated() 
            )
            .formLogin(form -> form
                .loginPage("/login") 
                .loginProcessingUrl("/login") 
                .usernameParameter("email")   
                .passwordParameter("password") 
                // ログイン成功後は必ず /afterlogin へ飛ばす
                .defaultSuccessUrl("/afterlogin", true) 
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/") 
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}