package in.tech_camp.protospace_knt.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UserEntityTest {

    @Test
    public void testUserEntityGettersAndSetters() {
        // 1. 準備：テスト用のユーザーの箱（インスタンス）を作成
        UserEntity user = new UserEntity();

        // 2. 実行：Setter（set...）を使って、ユーザーのプロフィール情報を注入
        // ※ もしあなたのUserEntityのidが「Integer型」なら 1 、「Long型」なら 1L に書き換えてください
        user.setId(1L); 
        user.setName("テスト太郎");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setProfile("こんにちは、Webエンジニアを目指しています。");
        user.setOccupation("学生");
        user.setPosition("初心者");

        // 3. 検証：Getter（get...）を使って、入れたデータがそのまま返ってくるか確認
        assertEquals(1L, user.getId());
        assertEquals("テスト太郎", user.getName());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("こんにちは、Webエンジニアを目指しています。", user.getProfile());
        assertEquals("学生", user.getOccupation());
        assertEquals("初心者", user.getPosition());
    }
}
