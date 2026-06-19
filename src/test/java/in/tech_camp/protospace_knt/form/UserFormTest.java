package in.tech_camp.protospace_knt.form;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UserFormTest {

    @Test
    public void testUserFormGettersAndSetters() {
        // 1. 準備：テスト用のフォームの箱（インスタンス）を作成
        UserForm form = new UserForm();

        // 2. 実行：Setterを使って、画面の入力欄から送られてくる文字をシミュレートして注入
        form.setEmail("new_user@example.com");
        form.setPassword("password123");
        form.setPasswordConfirmation("password123"); // パスワード一致確認用
        form.setName("テストユーザー");
        form.setProfile("バックエンドエンジニアを目指して勉強中です！");
        form.setOccupation("学生");
        form.setPosition("求職中");

        // 3. 検証：Getterを使って、入れた文字がそのまま取り出せるか確認
        assertEquals("new_user@example.com", form.getEmail());
        assertEquals("password123", form.getPassword());
        assertEquals("password123", form.getPasswordConfirmation());
        assertEquals("テストユーザー", form.getName());
        assertEquals("バックエンドエンジニアを目指して勉強中です！", form.getProfile());
        assertEquals("学生", form.getOccupation());
        assertEquals("求職中", form.getPosition());
    }
}
