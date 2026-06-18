package in.tech_camp.protospace_knt.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import in.tech_camp.protospace_knt.entity.CommentEntity;
import in.tech_camp.protospace_knt.repository.CommentRepository;

@WebMvcTest(CommentController.class) // CommentControllerだけを標的にテスト環境を構築
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc; // 画面からのリクエストを疑似再現する相棒

    @MockBean
    private CommentRepository commentRepository; // 本物のDBの代わりに動くモック（身代わり）

    @Test
    void 正常系_コメントが正しく入力されている場合_保存されて詳細ページにリダイレクトすること() throws Exception {
        // Arrange（準備）
        Long prototypeId = 10L;
        String commentText = "素晴らしいプロトタイプですね！";

        // Act（実行） & Assert（検証）
        mockMvc.perform(post("/protos/" + prototypeId + "/comments")
                .param("commentText", commentText)) // フォームの入力データをシミュレート
                .andExpect(status().is3xxRedirection()) // 302リダイレクトが発生するか
                .andExpect(redirectedUrl("/prototypes/" + prototypeId)); // リダイレクト先URLの検証

        // データベースにちゃんと保存処理が呼ばれたかを検証
        verify(commentRepository, times(1)).save(any(CommentEntity.class));
    }

    @Test
    void 異常系_バリデーションエラーがある場合_保存されずに詳細ページにリダイレクトすること() throws Exception {
        // Arrange（準備）
        Long prototypeId = 10L;
        String emptyCommentText = ""; // 空文字（バリデーションエラーを想定）

        // Act（実行） & Assert（検証）
        mockMvc.perform(post("/protos/" + prototypeId + "/comments")
                .param("commentText", emptyCommentText))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/prototypes/" + prototypeId));

        // エラーなので、データベースへの保存処理は「一度も呼ばれていない（times(0)）」ことを検証
        verify(commentRepository, times(0)).save(any(CommentEntity.class));
    }
}