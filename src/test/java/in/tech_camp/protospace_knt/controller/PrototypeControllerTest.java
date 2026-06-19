package in.tech_camp.protospace_knt.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import in.tech_camp.protospace_knt.entity.UserEntity;
import in.tech_camp.protospace_knt.repository.PrototypeRepository;
import in.tech_camp.protospace_knt.repository.UserRepository;

@WebMvcTest(PrototypeController.class)
public class PrototypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrototypeRepository prototypeRepository;

    @MockBean
    private UserRepository userRepository;

    private UserEntity mockUser;

    @BeforeEach
    public void setUp() {
        // テスト用のダミーユーザーを用意
        mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setName("テスト太郎");
        mockUser.setEmail("test@example.com");
    }

    // 1. ログイン後のページ表示のテスト
    @Test
    public void testShowAfterLogin() throws Exception {
        // UserRepositoryが呼ばれたら、用意したダミーユーザーを返すように設定（モック化）
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
        when(prototypeRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/afterlogin")
                .with(user("test@example.com"))) // ログイン状態を擬似的に再現
                .andExpect(status().isOk()) // ステータスコードが200(OK)であること
                .andExpect(view().name("afterlogin")) // afterlogin.htmlが返されること
                .andExpect(model().attribute("username", "テスト太郎")) // 画面に渡すデータが正しいこと
                .andExpect(model().attribute("userId", 1L));
    }

    // 2. 新規投稿画面の表示のテスト
    @Test
    public void testShowNewPrototype() throws Exception {
        mockMvc.perform(get("/protos/new")
                .with(user("test@example.com")))
                .andExpect(status().isOk())
                .andExpect(view().name("protos/new"))
                .andExpect(model().attributeExists("prototypeForm")); // 空のフォームが存在すること
    }

    // 3. 投稿内容の保存処理のテスト（画像アップロードを含む）
    @Test
    public void testCreatePrototype() throws Exception {
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        // テスト用のダミー画像ファイルを作成
        MockMultipartFile mockFile = new MockMultipartFile(
                "imageFile", 
                "test.jpg", 
                "image/jpeg", 
                "dummy image content".getBytes()
        );

        mockMvc.perform(multipart("/protos/new") // ファイル送信時はmultipartを使用
                .file(mockFile)
                .param("title", "作品")
                .param("catchCopy", "キャッチコピー")
                .param("concept", "コンセプト")
                .with(user("test@example.com"))
                .with(csrf())) // Spring SecurityのCSRF対策を突破するために必要
                .andExpect(status().is3xxRedirection()) // 保存後はリダイレクトされること
                .andExpect(redirectedUrl("/afterlogin")); // リダイレクト先が正しいこと

        // 実際にリポジトリの保存処理（insert）が1回呼び出されたかを検証
        verify(prototypeRepository, times(1)).insert(any());
    }

    // 4. 削除処理のテスト
    @Test
    public void testDeletePrototype() throws Exception {
        mockMvc.perform(post("/prototypes/delete")
                .param("id", "1")
                .with(user("test@example.com"))
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/afterlogin"));

        // 削除処理（deleteById）が正しく呼ばれたかを検証
        verify(prototypeRepository, times(1)).deleteById(1L);
    }
}