package in.tech_camp.protospace_knt.form;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PrototypeFormTest {

    @Test
    public void testPrototypeFormGettersAndSetters() {
        // 1. 準備：テスト用のフォームの箱（インスタンス）を作成
        PrototypeForm form = new PrototypeForm();

        // 2. 実行：Setterを使って画面の入力欄から送られてくる文字をシミュレートして注入
        form.setTitle("アプリ");
        form.setCatchCopy("便利に");
        form.setConcept("ツール");

        // 3. 検証：Getterを使って、入れた文字がそのまま取り出せるか確認
        assertEquals("アプリ", form.getTitle());
        assertEquals("便利に", form.getCatchCopy());
        assertEquals("ツール", form.getConcept());
    }
}