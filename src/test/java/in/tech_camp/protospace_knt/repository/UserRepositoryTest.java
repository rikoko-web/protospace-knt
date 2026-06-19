package in.tech_camp.protospace_knt.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.protospace_knt.entity.UserEntity;

@SpringBootTest
@Transactional // テストが終わったら、追加したダミーユーザーを自動でデータベースから消去（ロールバック）する
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testInsertAndFindByEmail() {
        // 1. 準備：テスト用の新しいユーザーデータを作成
        UserEntity user = new UserEntity();
        user.setEmail("repository_test@example.com");
        user.setPassword("secureOrder123");
        user.setName("リポ太郎");
        user.setProfile("リポジトリのテスト中。");
        user.setOccupation("テスター");
        user.setPosition("検証担当");

        // 2. 実行：データベースにユーザーを保存
        // ※ もし保存用メソッド名が「save」などの場合は書き換えてください
        userRepository.insert(user);

        // 3. 実行：ログイン処理をシミュレートして、メールアドレスでユーザーを検索
        UserEntity foundUser = userRepository.findByEmail("repository_test@example.com");

        // 4. 検証：検索結果が空（null）でなく、保存した内容と一致するか確認
        assertNotNull(foundUser);
        assertEquals("repository_test@example.com", foundUser.getEmail());
        assertEquals("secureOrder123", foundUser.getPassword());
        assertEquals("リポ太郎", foundUser.getName());
        assertEquals("リポジトリのテスト中。", foundUser.getProfile());
        assertEquals("テスター", foundUser.getOccupation());
        assertEquals("検証担当", foundUser.getPosition());
    }

    @Test
    public void testFindByEmail_NotFound() {
        // 存在しないメールアドレスで検索したとき、正しく null が返ってくるか検証
        UserEntity notFoundUser = userRepository.findByEmail("nobody@example.com");
        assertNull(notFoundUser);
    }
}