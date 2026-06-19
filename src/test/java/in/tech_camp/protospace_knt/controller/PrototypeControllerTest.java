package in.tech_camp.protospace_knt.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication; // 🟢 追加：Spring Securityの認証モック用
import org.springframework.test.web.servlet.MockMvc;

import in.tech_camp.protospace_knt.entity.PrototypeEntity;
import in.tech_camp.protospace_knt.entity.UserEntity;
import in.tech_camp.protospace_knt.repository.CommentRepository;
import in.tech_camp.protospace_knt.repository.PrototypeRepository;
import in.tech_camp.protospace_knt.repository.UserRepository;
import in.tech_camp.protospace_knt.service.UserService;

// 🟢 セキュリティ自動構成を完全に除外する設定
@WebMvcTest(controllers = PrototypeController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
public class PrototypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PrototypeRepository prototypeRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private UserService userService;

    private UserEntity mockUser;
    private Authentication mockAuthentication; // 🟢 PrincipalからAuthenticationに変更

    @BeforeEach
    public void setUp() {
        mockUser = new UserEntity();
        mockUser.setId(1); 
        mockUser.setName("テスト太郎");
        mockUser.setEmail("test@example.com");

        // 🟢 コントローラーが「認証済みの型」として受け取れるモックを作成
        mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getName()).thenReturn("test@example.com");
        when(mockAuthentication.getPrincipal()).thenReturn("test@example.com");
    }

    // 1. トップページ
    @Test
    public void testIndex_Authenticated() throws Exception {
        when(prototypeRepository.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("messages/index"));
    }

    // 2. ログイン画面表示
    @Test
    public void testShowLoginForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/login"));
    }

    // 3. ユーザー詳細画面
    @Test
    public void testShowUser() throws Exception {
        when(userRepository.findById(1L)).thenReturn(mockUser);
        when(prototypeRepository.findByUserId(1L)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/show"));
    }

    // 4. 新規ユーザー登録の成功
    @Test
    public void testRegisterUser_Success() throws Exception {
        mockMvc.perform(post("/signUp")
                .param("email", "new@example.com")
                .param("password", "password123")
                .param("passwordConfirmation", "password123")
                .param("name", "新規ユーザー"))
                .andExpect(status().isOk());
    }

    // 5. プロトタイプ詳細画面（Thymeleafクラッシュ対策）
    @Test
    public void testShowPrototypeDetail() throws Exception {
        PrototypeEntity mockPrototype = new PrototypeEntity();
        mockPrototype.setId(100);
        mockPrototype.setTitle("革新的なWebサービス");
        mockPrototype.setUser(mockUser);

        when(prototypeRepository.findById(100L)).thenReturn(mockPrototype);
        when(commentRepository.findByPrototypeId(100L)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/prototypes/100")
                .requestAttr("_csrf", new org.springframework.security.web.csrf.DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", "abc")))
                .andExpect(status().isOk())
                .andExpect(view().name("protos/detail"));
    }

    // 6. プロトタイプ新規投稿
    @Test
    public void testCreatePrototype() throws Exception {
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        MockMultipartFile mockFile = new MockMultipartFile(
                "imageFile", "test.jpg", "image/jpeg", "dummy".getBytes()
        );

        mockMvc.perform(multipart("/protos/new")
                .file(mockFile)
                .param("title", "革新的なWebサービス")
                .param("catchCopy", "キャッチコピー")
                .param("concept", "コンセプト")
                .principal(mockAuthentication)) // 🟢 修正：正しい認証モックオブジェクトを仕込む
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/afterlogin"));
    }
}