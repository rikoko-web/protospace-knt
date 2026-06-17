package in.tech_camp.protospace_knt.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.protospace_knt.entity.UserEntity;
import in.tech_camp.protospace_knt.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

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

    // 【2. ログイン時の照合処理】（Spring Security用）
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

    // ==========================================
    // 💡 ここから下が新しく追加されたマイページ用の処理です
    // ==========================================

    // 【3. メールアドレスからユーザー情報を取得】（ログイン中の本人特定用）
    public UserEntity findByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("ユーザーが見つかりません: " + email);
        }
        return user;
    }

    // 【4. IDからユーザー情報を取得】（詳細画面の表示用）
    public UserEntity findById(Long id) {
        UserEntity user = userRepository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("該当するユーザーが見つかりません。ID: " + id);
        }
        return user;
    }
}