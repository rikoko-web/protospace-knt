package in.tech_camp.protospace_knt.service;

import in.tech_camp.protospace_knt.entity.UserEntity;
import in.tech_camp.protospace_knt.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService { // ← implementsを追加

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 【1. 新規登録処理】
    public void registerUser(UserEntity user) {
        // パスワードを暗号化
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        
        // データベースに保存
        userRepository.save(user);
    }

    // 【2. ログイン時の照合処理】（Spring Security用に追加）
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 入力されたメールアドレスでDBからユーザーを検索
        UserEntity user = userRepository.findByEmail(email);
        
        // ユーザーが見つからなければエラーを投げる
        if (user == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりません: " + email);
        }

        // Spring Securityが理解できる「UserDetails」の形に変換して返す
        return new User(
            user.getEmail(),
            user.getPassword(),
            Collections.emptyList()
        );
    }
}