package in.tech_camp.protospace_knt.entity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

public class PrototypeEntityTest {

    @Test
    public void testPrototypeEntityGettersAndSetters() {
        // 1. 準備：テスト対象のオブジェクトと、中に詰めるダミーデータを用意
        PrototypeEntity prototype = new PrototypeEntity();
        UserEntity mockUser = new UserEntity();
        LocalDateTime now = LocalDateTime.now();

        mockUser.setId(10);
        mockUser.setName("開発太郎");

        // 2. 実行：Setter（set...）を使って、値を1つずつ格納する
        prototype.setId(1);
        prototype.setTitle("革新的なWebサービス");
        prototype.setCatchCopy("アイデア");
        prototype.setConcept("システム");
        prototype.setImage("/uploads/sample.jpg");
        prototype.setCreatedAt(now);
        prototype.setUserId(10);
        prototype.setUser(mockUser); // 🟢 関連付けられたユーザー情報を格納

        // 3. 検証：Getter（get...）で、格納した値がそのまま返ってくるか確認
        assertEquals(1, prototype.getId());
        assertEquals("革新的なWebサービス", prototype.getTitle());
        assertEquals("アイデア", prototype.getCatchCopy());
        assertEquals("システム", prototype.getConcept());
        assertEquals("/uploads/sample.jpg", prototype.getImage());
        assertEquals(now, prototype.getCreatedAt());
        assertEquals(10, prototype.getUserId());

        // 🟢 ユーザー情報の検証
        assertNotNull(prototype.getUser());
        assertEquals(10, prototype.getUser().getId());
        assertEquals("開発太郎", prototype.getUser().getName());
    }
}
