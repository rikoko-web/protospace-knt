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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import in.tech_camp.protospace_knt.entity.PrototypeEntity;
import in.tech_camp.protospace_knt.entity.UserEntity;
import in.tech_camp.protospace_knt.form.PrototypeForm;
import in.tech_camp.protospace_knt.repository.PrototypeRepository;
import in.tech_camp.protospace_knt.repository.UserRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PrototypeController {

    private final PrototypeRepository prototypeRepository;
    private final UserRepository userRepository;

    // 1. ログイン後のページ表示（全員の投稿を表示）
    @GetMapping("/afterlogin")
    public String showAfterLogin(Model model, Authentication auth) {
        String email = auth.getName();
        UserEntity user = userRepository.findByEmail(email);
        
        // 修正：username と userId を両方モデルに追加
        model.addAttribute("username", user.getName());
        model.addAttribute("userId", user.getId()); 
        
        // 全ユーザーの投稿一覧を取得
        model.addAttribute("prototypes", prototypeRepository.findAll());
        
        return "afterlogin";
    }

    // 2. 投稿画面の表示
    @GetMapping("/protos/new")
    public String showNewPrototype(Model model) {
        model.addAttribute("prototypeForm", new PrototypeForm());
        return "protos/new";
    }

    // 3. 投稿内容の保存処理
    @PostMapping("/protos/new")
    public String createPrototype(
            @ModelAttribute("prototypeForm") PrototypeForm form, 
            @RequestParam("imageFile") MultipartFile imageFile,
            Authentication auth) {
        
        // 画像の保存処理
        String imageWebPath = null;
        if (!imageFile.isEmpty()) {
            try {
                Path uploadPath = Paths.get("build/resources/main/static/uploads/").toAbsolutePath().normalize();
                if (!Files.exists(uploadPath)) { Files.createDirectories(uploadPath); }
                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                imageWebPath = "/uploads/" + fileName;
            } catch (IOException e) {
                e.printStackTrace();
                return "protos/new";
            }
        }

        // データベースへの保存
        String email = auth.getName();
        UserEntity user = userRepository.findByEmail(email);
        
        PrototypeEntity prototype = new PrototypeEntity();
        prototype.setTitle(form.getTitle());
        prototype.setCatchCopy(form.getCatchCopy());
        prototype.setConcept(form.getConcept());
        prototype.setImage(imageWebPath);
        prototype.setUserId(user.getId());
        
        prototypeRepository.insert(prototype);
        
        // 保存後は afterlogin ページへ移動する
        return "redirect:/afterlogin";
    }

    // 🛠️ 4. 【ここに新しく追加！】投稿の削除処理
    @PostMapping("/prototypes/delete")
    public String deletePrototype(@RequestParam("id") Long id) {
        // 先ほど Repository に追加した deleteById を呼び出してデータを消す
        prototypeRepository.deleteById(id);
        
        // 削除が完了したら一覧画面（afterlogin）へ戻る
        return "redirect:/afterlogin";
    }
   }
