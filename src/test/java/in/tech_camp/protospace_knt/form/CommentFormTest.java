package in.tech_camp.protospace_knt.form;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CommentFormTest {

    @Test
    public void testCommentFormGettersAndSetters() {
        // 1. 準備：テスト用のフォームの箱（インスタンス）を作成
        CommentForm form = new CommentForm();

        // 2. 実行：Setterを使って画面から送られてくる文字をシミュレートして注入
        form.setCommentText("とても参考になるプロトタイプですね！");

        // 3. 検証：Getterを使って、入れた文字がそのまま取り出せるか確認
        assertEquals("とても参考になるプロトタイプですね！", form.getCommentText());
    }
}