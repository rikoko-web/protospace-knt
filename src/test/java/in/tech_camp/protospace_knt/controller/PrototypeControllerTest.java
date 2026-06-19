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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import in.tech_camp.protospace_knt.entity.PrototypeEntity;
import in.tech_camp.protospace_knt.entity.UserEntity;
import in.tech_camp.protospace_knt.repository.CommentRepository;
import in.tech_camp.protospace_knt.repository.PrototypeRepository;
import in.tech_camp.protospace_knt.repository.UserRepository;
import in.tech_camp.protospace_knt.service.UserService;

// 🟢 pom.xmlを変更せずに動かすため、テスト時だけセキュリティ自動構成をオフにする
@WebMvcTest(controllers = PrototypeController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
public class PrototypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // コントローラーが依存しているリポジトリやサービスをすべてモック（偽物）にする
    @MockBean
    private PrototypeRepository prototypeRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private UserService userService;

    private UserEntity mockUser;
    private Authentication mockAuth;

    @BeforeEach
    public void setUp() {
        // ダミーユーザーの作成
        mockUser = new UserEntity();
        mockUser.setId(1);
        mockUser.setName("テスト太郎");
        mockUser.setEmail("test@example.com");

        // ダミーのログイン状態の作成（pom.xmlに依存しない標準クラスを使用）
        mockAuth = new UsernamePasswordAuthenticationToken("test@example.com", "password");
    }

    // --- 1. トップページ・ログイン関連のテスト ---
    @Test
    public void testIndex_Authenticated() throws Exception {
        when(prototypeRepository.findAll()).thenReturn(new ArrayList<>());
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        mockMvc.perform(get("/")
                .principal(mockAuth)) // ログイン状態でアクセス
                .andExpect(status().isOk())
                .andExpect(view().name("messages/index"))
                .andExpect(model().attribute("username", "テスト太郎"))
                .andExpect(model().attribute("userId", 1L));
    }

    @Test
    public void testShowLoginForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/login"));
    }

    // --- 2. ユーザー関連のテスト ---
    @Test
    public void testShowUser() throws Exception {
        when(userRepository.findById(1L)).thenReturn(mockUser);
        when(prototypeRepository.findByUserId(1L)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("users/show"))
                .andExpect(model().attribute("user", mockUser))
                .andExpect(model().attributeExists("userPrototypes"));
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        mockMvc.perform(post("/signUp")
                .param("email", "new@example.com")
                .param("password", "password123")
                .param("passwordConfirmation", "password123")
                .param("name", "新規ユーザー"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        // userServiceの登録メソッドが1回呼ばれたことを検証
        verify(userService, times(1)).registerUser(any(UserEntity.class));
    }

    // --- 3. プロトタイプ関連のテスト ---
    @Test
    public void testShowPrototypeDetail() throws Exception {
        PrototypeEntity mockPrototype = new PrototypeEntity();
        mockPrototype.setId(100);
        mockPrototype.setTitle("テスト作品");

        when(prototypeRepository.findById(100L)).thenReturn(mockPrototype);
        when(commentRepository.findByPrototypeId(100L)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/prototypes/100"))
                .andExpect(status().isOk())
                .andExpect(view().name("protos/detail"))
                .andExpect(model().attribute("prototype", mockPrototype))
                .andExpect(model().attributeExists("commentForm"))
                .andExpect(model().attributeExists("comments"));
    }

    @Test
    public void testCreatePrototype() throws Exception {
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        MockMultipartFile mockFile = new MockMultipartFile(
                "imageFile", "test.jpg", "image/jpeg", "dummy".getBytes()
        );

        mockMvc.perform(multipart("/protos/new")
                .file(mockFile)
                .param("title", "プロトタイプタイトル")
                .param("catchCopy", "キャッチコピー")
                .param("concept", "コンセプト")
                .principal(mockAuth))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/afterlogin"));

        verify(prototypeRepository, times(1)).insert(any(PrototypeEntity.class));
    }
}