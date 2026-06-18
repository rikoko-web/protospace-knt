package in.tech_camp.protospace_knt.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_knt.entity.PrototypeEntity;
import in.tech_camp.protospace_knt.entity.UserEntity;
import in.tech_camp.protospace_knt.entity.CommentEntity; // ★追加
import in.tech_camp.protospace_knt.form.CommentForm;
import in.tech_camp.protospace_knt.form.UserForm;
import in.tech_camp.protospace_knt.repository.PrototypeRepository;
import in.tech_camp.protospace_knt.repository.UserRepository;
import in.tech_camp.protospace_knt.repository.CommentRepository; // ★追加
import in.tech_camp.protospace_knt.service.UserService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PrototypeRepository prototypeRepository;
    private final CommentRepository commentRepository; // ★追加（コメントリポジトリの登録）

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

    // --- ユーザー詳細画面表示 ---
    @GetMapping("/users/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        UserEntity user = userRepository.findById(id);
        model.addAttribute("user", user);

        List<PrototypeEntity> userPrototypes = prototypeRepository.findByUserId(id);
        model.addAttribute("userPrototypes", userPrototypes);

        return "users/show";
    }

    // ★★★ プロトタイプ詳細画面表示（修正版） ★★★
    @GetMapping("/prototypes/{id}")
    public String showPrototypeDetail(@PathVariable("id") Long id, Model model) {
        // 【修正】findByUserId ではなく、プロトタイプIDで直接1件取得する
        PrototypeEntity prototype = prototypeRepository.findById(id);
        
        // 安全対策：もしデータが見つからなかった場合に空のオブジェクトを入れる
        if (prototype == null) {
            prototype = new PrototypeEntity();
            prototype.setUser(new UserEntity()); // 画面での「user.id」のnullエラーを防ぐ
        }
        
        model.addAttribute("prototype", prototype);

        // commentText を持った CommentForm を渡す
        model.addAttribute("commentForm", new CommentForm()); 

        // コメント一覧をリポジトリから取得して画面に渡す
        try {
            List<CommentEntity> comments = commentRepository.findByPrototypeId(id);
            model.addAttribute("comments", comments);
        } catch (Exception e) {
            // もしエラーが発生した場合は、空のリストを渡して画面エラーを防ぐ
            model.addAttribute("comments", java.util.Collections.emptyList());
        }

        return "protos/detail"; 
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