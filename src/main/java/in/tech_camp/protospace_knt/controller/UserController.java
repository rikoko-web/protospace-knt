package in.tech_camp.protospace_knt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/")
    public String index(Model model) {
        // List<UserEntity> prototypes = userRepository.findAll();
       model.addAttribute("prototypes", java.util.Collections.emptyList());
        return "messages/index";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "users/login"; 
    }

    // 💡 1. 画面のボタンに合わせて、URLを「/user/new」に変更します
    @GetMapping("/signUp")
    public String showSignupForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "users/signUp"; // 表示するHTMLは「signUp.html」のままで100%正解です
    }

    // 💡 2. 送信先のURLも「/user/new」に変更します
    @PostMapping("/signUp")
    public String registerUser(@Validated @ModelAttribute("userForm") UserForm userForm, 
                               BindingResult bindingResult, 
                               Model model) {
        
        // 💡 3. 要件定義の「パスワード確認用一致」のチェックを追加します
        if (userForm.getPassword() != null && !userForm.getPassword().equals(userForm.getPasswordConfirmation())) {
            bindingResult.rejectValue("passwordConfirmation", "error.passwordConfirmation", "パスワードと確認用パスワードが一致しません");
        }

        // 入力チェック（必須や形式エラー、または上記の一致エラー）があれば新規登録画面に留まる
        if (bindingResult.hasErrors()) {
            return "users/signUp";
        }

        // フォームからエンティティへの詰め替え（DBeaverの定義に完全一致）
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userForm.getEmail());
        userEntity.setPassword(userForm.getPassword());
        userEntity.setName(userForm.getName()); 
        userEntity.setProfile(userForm.getProfile());
        userEntity.setOccupation(userForm.getOccupation()); 
        userEntity.setPosition(userForm.getPosition()); 

        // データベースに登録を実行
        userService.registerUser(userEntity);
        
        return "redirect:/login";
    }
}