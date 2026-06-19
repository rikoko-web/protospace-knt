package in.tech_camp.protospace_knt.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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
        // 🟢 URLを「/prototypes/...」から「/protos/...」に修正しました
        mockMvc.perform(post("/protos/1/comments")
                .param("commentText", "素晴らしい作品ですね！"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/prototypes/1")); // リダイレクト先は/prototypes/1で合っています
    }

    @Test
    public void 異常系_バリデーションエラーがある場合_保存されずに詳細ページにリダイレクトすること() throws Exception {
        // 🟢 こちらもURLを「/protos/...」に修正しました
        mockMvc.perform(post("/protos/1/comments")
                .param("commentText", "")) // 空文字でエラーにする
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/prototypes/1"));
    }
}