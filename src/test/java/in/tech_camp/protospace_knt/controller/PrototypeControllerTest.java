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
    private Authentication mockAuth; 

    @BeforeEach
    public void setUp() {
        // テスト用のダミーユーザーを用意
        mockUser = new UserEntity();
        // もし setId でまだ赤線が出る場合は、この行をコメントアウト(「//」を先頭に付ける)しても動きます
        mockUser.setId(1L); 
        mockUser.setName("テスト太郎");
        mockUser.setEmail("test@example.com");

        
        mockAuth = new UsernamePasswordAuthenticationToken("test@example.com", "password");
    }

    // 1. ログイン後のページ表示のテスト
    @Test
    public void testShowAfterLogin() throws Exception {
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
        when(prototypeRepository.findAll()).thenReturn(new ArrayList<>());

        
        mockMvc.perform(get("/afterlogin")
                .principal(mockAuth)) 
                .andExpect(status().isOk())
                .andExpect(view().name("afterlogin"))
                .andExpect(model().attribute("username", "テスト太郎"));
    }

    // 2. 新規投稿画面の表示のテスト
    @Test
    public void testShowNewPrototype() throws Exception {
        mockMvc.perform(get("/protos/new")
                .principal(mockAuth))
                .andExpect(status().isOk())
                .andExpect(view().name("protos/new"));
    }

    // 3. 投稿内容の保存処理のテスト
    @Test
    public void testCreatePrototype() throws Exception {
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        MockMultipartFile mockFile = new MockMultipartFile(
                "imageFile", "test.jpg", "image/jpeg", "dummy".getBytes()
        );

        
        mockMvc.perform(multipart("/protos/new")
                .file(mockFile)
                .param("title", "新しい作品")
                .param("catchCopy", "コピー")
                .param("concept", "コンセプト")
                .principal(mockAuth))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/afterlogin"));
    }
}