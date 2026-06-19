package in.tech_camp.protospace_knt.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.protospace_knt.entity.CommentEntity;

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
        comment.setUserId(1L);        // テスト用のユーザーID（実在する、または仮のID）
        comment.setPrototypeId(999L); // テスト対象のプロトタイプID

        // 2. 実行：リポジトリを使ってデータベースに保存（保存処理の名前が異なる場合は適宜変更してください）
        commentRepository.insert(comment);

        // 3. 実行：保存したプロトタイプID（999）を指定して、データベースからコメントを取得
        List<CommentEntity> comments = commentRepository.findByPrototypeId(999L);

        // 4. 検証：正しく保存され、取り出せたかを確認
        assertNotNull(comments); // リストが空っぽ（null）じゃないこと
        assertFalse(comments.isEmpty()); // リストの中身が1つ以上あること
        
        // 1つ目のコメントを取り出して、中身が一致するか確認
        CommentEntity savedComment = comments.get(0);
        assertEquals("テスト用の素晴らしいコメントです", savedComment.getCommentText());
        assertEquals(1L, savedComment.getUserId());
        assertEquals(999L, savedComment.getPrototypeId());
    }
}
