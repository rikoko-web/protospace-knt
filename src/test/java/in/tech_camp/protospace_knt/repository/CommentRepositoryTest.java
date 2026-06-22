package in.tech_camp.protospace_knt.repository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.protospace_knt.entity.CommentEntity;
import in.tech_camp.protospace_knt.entity.UserEntity;

@SpringBootTest // アプリの全設定（MyBatisなど）を読み込んでテストする設定
@Transactional  // 🟢 超重要：テストが終わったら、追加したデータを自動で消して元通りにする
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void testInsertAndFindByPrototypeId() {
        // 1. 準備：テスト用のコメントデータを作成
        CommentEntity comment = new CommentEntity();
        comment.setCommentText("テスト用の素晴らしいコメントです");
        
        // 🌟【修正】実在する可能性が高い ID: 1L に変更します
        comment.setUserId(1L);        
        comment.setPrototypeId(1L);   // 999L から 1L に変更！

        // 🌟【追加】前回のエンティティ追加に伴う対策
        comment.setUser(new UserEntity()); 

        // 2. 実行：リポジトリを使ってデータベースに保存
        commentRepository.save(comment);

        // 3. 実行：【修正】保存したプロトタイプID（1L）を指定して取得
        List<CommentEntity> comments = commentRepository.findByPrototypeId(1L); // 1L に変更！

        // 4. 検証：正しく保存され、取り出せたかを確認
        assertNotNull(comments); 
        assertFalse(comments.isEmpty()); 
        
        CommentEntity savedComment = comments.get(0);
        assertEquals("テスト用の素晴らしいコメントです", savedComment.getCommentText());
        assertEquals(1L, savedComment.getUserId());
        assertEquals(1L, savedComment.getPrototypeId()); // 🌟 1L に変更！
    }
}
