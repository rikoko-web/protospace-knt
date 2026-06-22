package in.tech_camp.protospace_knt.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import in.tech_camp.protospace_knt.entity.PrototypeEntity;
import in.tech_camp.protospace_knt.entity.UserEntity;
import in.tech_camp.protospace_knt.form.CommentForm;
import in.tech_camp.protospace_knt.form.PrototypeForm;
import in.tech_camp.protospace_knt.form.UserForm;
import in.tech_camp.protospace_knt.repository.CommentRepository;
import in.tech_camp.protospace_knt.repository.PrototypeRepository;
import in.tech_camp.protospace_knt.repository.UserRepository;
import in.tech_camp.protospace_knt.service.UserService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PrototypeController {

    private final PrototypeRepository prototypeRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    // --- 1. トップページ・ログイン関連 ---
    @GetMapping("/")
    public String index(Model model, Authentication auth) {
        model.addAttribute("prototypes", prototypeRepository.findAll());
        if (auth != null && auth.isAuthenticated()) {
            UserEntity user = userRepository.findByEmail(auth.getName());
            if (user != null) {
                model.addAttribute("username", user.getName());
                model.addAttribute("userId", user.getId());
            }
        }
        return "messages/index";
    }

    // ログイン後画面の追加
    @GetMapping("/afterlogin")
    public String showAfterLogin(Model model, Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            UserEntity user = userRepository.findByEmail(auth.getName());
            if (user != null) {
                model.addAttribute("username", user.getName());
                model.addAttribute("userId", user.getId());
            }
        }
        model.addAttribute("prototypes", prototypeRepository.findAll());
        return "afterlogin";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "users/login";
    }

    // --- 2. ユーザー関連 ---
    @GetMapping("/users/{id}")
    public String showUser(@PathVariable("id") Long id, Model model) {
        UserEntity user = userRepository.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("userPrototypes", prototypeRepository.findByUserId(id));
        return "users/show";
    }

    @GetMapping("/signUp")
    public String showSignupForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "users/signUp";
    }

    @PostMapping("/signUp")
    public String registerUser(@Validated @ModelAttribute("userForm") UserForm userForm,
                               BindingResult bindingResult, Model model) {
        if (userForm.getPassword() != null && !userForm.getPassword().equals(userForm.getPasswordConfirmation())) {
            bindingResult.rejectValue("passwordConfirmation", "error.passwordConfirmation", "パスワードが一致しません");
        }
        if (bindingResult.hasErrors()) return "users/signUp";

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

    // --- 3. プロトタイプ詳細・表示・投稿・編集 ---
    @GetMapping("/prototypes/{id}")
    public String showPrototypeDetail(@PathVariable("id") Long id, Model model) {
        PrototypeEntity prototype = prototypeRepository.findById(id);
        if (prototype == null) {
            prototype = new PrototypeEntity();
            prototype.setUser(new UserEntity());
        }
        model.addAttribute("prototype", prototype);
        model.addAttribute("commentForm", new CommentForm());
        try {
            model.addAttribute("comments", commentRepository.findByPrototypeId(id));
        } catch (Exception e) {
            model.addAttribute("comments", java.util.Collections.emptyList());
        }
        return "protos/detail";
    }

    @GetMapping("/protos/new")
    public String showNewPrototype(Model model) {
        model.addAttribute("prototypeForm", new PrototypeForm());
        return "protos/new";
    }

    @PostMapping("/protos/new")
    public String createPrototype(@Validated @ModelAttribute("prototypeForm") PrototypeForm form, // ★ @Validated を追加
                                  BindingResult bindingResult, // ★ 引数にこれを追加（Formオブジェクトの直後に配置）
                                  @RequestParam("imageFile") MultipartFile imageFile,
                                  Authentication auth) {
        
        // ★ バリデーションエラーがある場合、処理を中断して投稿フォームに戻る処理を追加
        if (bindingResult.hasErrors()) {
            return "protos/new";
        }

        String imageWebPath = imageFile.isEmpty() ? null : saveImage(imageFile);
        
        UserEntity user = userRepository.findByEmail(auth.getName());
        PrototypeEntity prototype = new PrototypeEntity();
        prototype.setTitle(form.getTitle());
        prototype.setCatchCopy(form.getCatchCopy());
        prototype.setConcept(form.getConcept());
        prototype.setImage(imageWebPath);
        prototype.setUserId(user.getId());
        prototypeRepository.insert(prototype);

        return "redirect:/afterlogin";
    }

    // 🛠️ 画像の通りに修正・変更（削除処理）
    @PostMapping("/prototypes/delete")
    public String deletePrototype(@RequestParam("id") Long id, Authentication auth) {
        UserEntity user = userRepository.findByEmail(auth.getName());
        PrototypeEntity prototype = prototypeRepository.findById(id);

        if (prototype != null && user != null) {
            if (prototype.getUserId().longValue() != user.getId().longValue()) {
                return "redirect:/afterlogin";
            }
        }

        prototypeRepository.deleteById(id);
        return "redirect:/afterlogin";
    }

    // 🛠️ 画像の通りに修正・変更（編集ページ表示処理）
    @GetMapping("/protos/{id}/edit")
    public String showEditPrototype(@PathVariable("id") Long id, Model model, Authentication auth) {
        UserEntity user = userRepository.findByEmail(auth.getName());
        PrototypeEntity prototype = prototypeRepository.findById(id);

        if (prototype != null && user != null) {
            if (prototype.getUserId().longValue() != user.getId().longValue()) {
                return "redirect:/afterlogin";
            }
        }

        model.addAttribute("prototype", prototype);
        return "prototype_edit";
    }

    @PostMapping("/protos/{id}/update")
    public String updatePrototype(@PathVariable("id") Long id,
                                  @ModelAttribute("prototype") PrototypeEntity formEntity,
                                  @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        PrototypeEntity prototype = prototypeRepository.findById(id);
        if (prototype != null) {
            prototype.setTitle(formEntity.getTitle());
            prototype.setCatchCopy(formEntity.getCatchCopy());
            prototype.setConcept(formEntity.getConcept());
            if (imageFile != null && !imageFile.isEmpty()) {
                prototype.setImage(saveImage(imageFile));
            }
            prototypeRepository.update(prototype);
        }
        return "redirect:/prototypes/" + id;
    }

    private String saveImage(MultipartFile imageFile) {
        try {
            Path uploadPath = Paths.get("uploads/").toAbsolutePath().normalize();
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
            String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename().replaceAll("\\s+", "");
            Files.copy(imageFile.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}