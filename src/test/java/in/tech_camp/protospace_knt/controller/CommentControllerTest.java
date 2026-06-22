package in.tech_camp.protospace_knt.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import in.tech_camp.protospace_knt.entity.UserEntity;
import in.tech_camp.protospace_knt.repository.CommentRepository;
import in.tech_camp.protospace_knt.repository.UserRepository;

// テスト時のみセキュリティ自動構成を完全に除外する設定
@WebMvcTest(controllers = CommentController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void 正常系_コメントが正しく入力されている場合_保存されて詳細ページにリダイレクトすること() throws Exception {
        // --- ダミーの認証情報（Authentication）を設定 ---
        Authentication mockAuth = Mockito.mock(Authentication.class);
        Mockito.when(mockAuth.getName()).thenReturn("test@example.com");
        
        // --- ダミーのユーザー情報を設定（idの型エラー対策で 1L から 1 に変更） ---
        UserEntity dummyUser = Mockito.mock(UserEntity.class);
        Mockito.when(dummyUser.getId()).thenReturn(1); // ★ここを Integer の 1 に修正しました
        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(dummyUser);

        // 🟢 URLや検証内容は元のコードのままです
        mockMvc.perform(post("/protos/1/comments")
                .principal(mockAuth)
                .param("commentText", "素晴らしい作品ですね！"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/protos/1"));
    }

    @Test
    public void 異常系_バリデーションエラーがある場合_保存されずに詳細ページにリダイレクトすること() throws Exception {
        // 🟢 検証内容は元のコードのままです
        mockMvc.perform(post("/protos/1/comments")
                .param("commentText", "")) // 空文字でエラーにする
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/protos/1"));
    }
}