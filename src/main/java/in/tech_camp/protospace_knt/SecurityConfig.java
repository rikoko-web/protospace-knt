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
                // 新規登録やログイン画面などは未ログインでもアクセスOK
                .requestMatchers("/", "/login", "/signUp", "/css/**", "/images/**").permitAll() 
                
                // プロトタイプ詳細などのGETアクセスは未ログインでもOK
                .requestMatchers(HttpMethod.GET, "/prototypes/**", "/users/**").permitAll() 
                
                .anyRequest().authenticated() 
            )
            .formLogin(form -> form
                .loginPage("/login") 
                .loginProcessingUrl("/login") 
                .usernameParameter("email")   // login.htmlの name="email" と合わせる
                .passwordParameter("password") 
                .defaultSuccessUrl("/afterlogin", true) // ★ここを "/afterlogin" に変更しました
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