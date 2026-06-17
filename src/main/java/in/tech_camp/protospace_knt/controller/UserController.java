package in.tech_camp.protospace_knt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.tech_camp.protospace_knt.entity.UserEntity;
import in.tech_camp.protospace_knt.form.UserForm;
import in.tech_camp.protospace_knt.repository.UserRepository; 
import in.tech_camp.protospace_knt.service.UserService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService; 

    // --- トップページ ---
    @GetMapping("/")
    public String index(Model model) {
       model.addAttribute("prototypes", java.util.Collections.emptyList());
       return "messages/index";
    }

    // --- ログイン画面表示 ---
    @GetMapping("/login")
    public String showLoginForm() {
        return "users/login"; 
    }

    // --- 【修正済み】ログイン処理 ---
    // HTMLの name="username" と name="password" に合わせて引数を修正しました
    @PostMapping("/login")
    public String login(@RequestParam("username") String username, 
                        @RequestParam("password") String password, 
                        Model model) {
        
        // 動作確認：ログイン後の名前をセット
        model.addAttribute("username", username);
        
        // ログイン成功後に表示する画面へ遷移
        return "afterlogin";
    }

    // --- 新規登録画面表示 ---
    @GetMapping("/signUp")
    public String showSignupForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "users/signUp";
    }

    // --- 新規登録実行 ---
    @PostMapping("/signUp")
    public String registerUser(@Validated @ModelAttribute("userForm") UserForm userForm, 
                               BindingResult bindingResult, 
                               Model model) {
        
        if (userForm.getPassword() != null && !userForm.getPassword().equals(userForm.getPasswordConfirmation())) {
            bindingResult.rejectValue("passwordConfirmation", "error.passwordConfirmation", "パスワードと確認用パスワードが一致しません");
        }

        if (bindingResult.hasErrors()) {
            return "users/signUp";
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userForm.getEmail());
        userEntity.setPassword(userForm.getPassword());
        userEntity.setName(userForm.getName()); 
        userEntity.setProfile(userForm.getProfile());
        userEntity.setOccupation(userForm.getOccupation()); 
        userEntity.setPosition(userForm.getPosition()); 

        userService.registerUser(userEntity);
        
        return "redirect:/login";
    }
}