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
import in.tech_camp.protospace_knt.entity.PrototypeEntity;
import in.tech_camp.protospace_knt.entity.UserEntity;

@SpringBootTest
@Transactional  
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PrototypeRepository prototypeRepository;

    @Test
    public void testInsertAndFindByPrototypeId() {
        // 🌟 被らないテスト用のメールアドレスを決める
        String testEmail = "test_comment_user@example.com";

        // 【対策①】テスト用のユーザーを作成してDBに保存
        UserEntity user = new UserEntity();
        user.setName("テストユーザー");
        user.setEmail(testEmail);
        user.setPassword("password");
        user.setProfile("テストプロフィール");
        user.setOccupation("テスト職業");
        user.setPosition("テスト役職");
        
        userRepository.save(user); 

        // 🌟【超重要】DBから「今入れたユーザー」をメールアドレスで検索して、確実に発番されたIDを取得する
        // ※ もし findByEmail というメソッド名でなければ、お使いのUserRepositoryの検索メソッドに合わせてください
        UserEntity savedUserFromDb = userRepository.findByEmail(testEmail);
        Integer dbUserId = savedUserFromDb.getId(); // これで絶対に本物のID（30など）が取れます！

        // 【対策②】テスト用のプロトタイプを作成してDBに保存
        PrototypeEntity prototype = new PrototypeEntity();
        prototype.setUserId(dbUserId); // 確実に存在する本物のユーザーIDをセット！
        prototype.setTitle("テストタイトル");
        prototype.setCatchCopy("テストキャッチコピー");
        prototype.setConcept("テストコンセプト");
        
        prototypeRepository.insert(prototype); 

        // 🌟【超重要】プロトタイプも同様に、DBに自動発番された本物のIDを直接取りにいくのは難しいため、
        // 「このユーザーIDに紐づくプロトタイプ」をDBから逆引きして本物のIDを突き止めます
        // ※ もしリポジトリに適切な検索がなければ、一番シンプルな「全件取得」から最後の1件を取る方法で確実にIDを奪取します
        List<PrototypeEntity> allPrototypes = prototypeRepository.findAll(); // もしfindAllがなければ、プロトタイプを取得するメソッドに変えてください
        PrototypeEntity savedPrototypeFromDb = allPrototypes.get(allPrototypes.size() - 1);
        Integer dbPrototypeId = savedPrototypeFromDb.getId(); // これでプロトタイプの本物のIDも確定！

        // 1. 準備：テスト用のコメントデータを作成
        CommentEntity comment = new CommentEntity();
        comment.setCommentText("テスト用の素晴らしいコメントです");

        // 【対策③】確定した本物のIDをLong型に変換してセット
        Long userIdLong = Long.valueOf(dbUserId);
        Long prototypeIdLong = Long.valueOf(dbPrototypeId);

        comment.setUserId(userIdLong);
        comment.setPrototypeId(prototypeIdLong);   

        comment.setUser(savedUserFromDb);

        // 2. 実行：リポジトリを使ってデータベースに保存
        commentRepository.save(comment);

        // 3. 実行：保存したプロトタイプIDを指定して取得
        List<CommentEntity> comments = commentRepository.findByPrototypeId(prototypeIdLong); 

        // 4. 検証：正しく保存され、取り出せたかを確認
        assertNotNull(comments);
        assertFalse(comments.isEmpty());

        CommentEntity savedComment = comments.get(0);
        assertEquals("テスト用の素晴らしいコメントです", savedComment.getCommentText());
        assertEquals(userIdLong, savedComment.getUserId());
        assertEquals(prototypeIdLong, savedComment.getPrototypeId()); 
    }
}