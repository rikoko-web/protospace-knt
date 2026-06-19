package in.tech_camp.protospace_knt.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import in.tech_camp.protospace_knt.entity.PrototypeEntity;

@SpringBootTest
@Transactional // テストが終わったら、追加・変更・削除したデータを自動で元通り（ロールバック）にする
public class PrototypeRepositoryTest {

    @Autowired
    private PrototypeRepository prototypeRepository;

    @Test
    public void testPrototypeCrudOperations() {
        // --- 1. 新規保存（Insert）のテスト ---
        PrototypeEntity prototype = new PrototypeEntity();
        prototype.setTitle("テスト用プロトタイプ");
        prototype.setCatchCopy("テストのキャッチコピー");
        prototype.setConcept("テストのコンセプト");
        prototype.setImage("/uploads/test.jpg");
        prototype.setUserId(1); // テスト用のユーザーID（環境に合わせて適宜変更してください）

        // データベースに保存
        prototypeRepository.insert(prototype);

        // --- 2. 全件取得（findAll）のテスト ---
        List<PrototypeEntity> list = prototypeRepository.findAll();
        assertNotNull(list);
        assertFalse(list.isEmpty()); // 最低でも今入れた1件は入っていること

        // --- 3. 更新（Update）のテスト ---
        // 今保存したデータのIDを取得（MyBatisの仕様で自動生成されたIDがセットされます。もし型がLongなら 1L 等に調整してください）
        // ※上手くIDが取れない場合は、既存のレコードのIDを指定してもテスト可能です
        Integer targetId = prototype.getId();
        
        if (targetId != null) {
            PrototypeEntity foundPrototype = prototypeRepository.findById(targetId.longValue());
            assertNotNull(foundPrototype);
            
            // タイトルを書き換えてアップデートしてみる
            foundPrototype.setTitle("更新されたタイトル");
            prototypeRepository.update(foundPrototype);
            
            // 再度取得して、本当に書き換わっているか確認
            PrototypeEntity updatedPrototype = prototypeRepository.findById(targetId.longValue());
            assertEquals("更新されたタイトル", updatedPrototype.getTitle());
            
            // --- 4. 削除（Delete）のテスト ---
            prototypeRepository.deleteById(targetId.longValue());
            PrototypeEntity deletedPrototype = prototypeRepository.findById(targetId.longValue());
            // 削除されたので、検索しても見つからない（null）であることを確認
            assertNull(deletedPrototype);
        }
    }
}