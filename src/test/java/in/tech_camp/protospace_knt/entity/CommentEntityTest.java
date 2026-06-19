package in.tech_camp.protospace_knt.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CommentEntityTest {

    @Test
    public void testCommentEntityGettersAndSetters() {
        // 1. 準備：テスト用のインスタンス（箱）を作成
        CommentEntity comment = new CommentEntity();
        UserEntity mockUser = new UserEntity();
        
        mockUser.setId(10L);
        mockUser.setName("テスト次郎");

        // 2. 実行：Setter（値をセットする命令）を使ってデータを注入
        comment.setId(1L);
        comment.setCommentText("素晴らしいプロトタイプですね！");
        comment.setUserId(2L);
        comment.setPrototypeId(3L);
        comment.setUser(mockUser); // 🟢 追加されたフィールドへのセット

        // 3. 検証：Getter（値を取り出す命令）で、入れたデータが正しく返ってくるか確認
        assertEquals(1L, comment.getId());
        assertEquals("素晴らしいプロトタイプですね！", comment.getCommentText());
        assertEquals(2L, comment.getUserId());
        assertEquals(3L, comment.getPrototypeId());
        
        // 🟢 追加されたユーザー情報の検証
        assertNotNull(comment.getUser());
        assertEquals(10L, comment.getUser().getId());
        assertEquals("テスト次郎", comment.getUser().getName());
    }
}