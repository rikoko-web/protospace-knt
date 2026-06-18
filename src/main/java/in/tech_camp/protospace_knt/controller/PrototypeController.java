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
import org.springframework.web.bind.annotation.PathVariable;
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
        
        model.addAttribute("username", user.getName());
        model.addAttribute("userId", user.getId()); 
        
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
            Authentication auth,
            Model model) { 
        
        String imageWebPath = null;
        if (!imageFile.isEmpty()) {
            try {
                Path uploadPath = Paths.get("build/resources/main/static/uploads/").toAbsolutePath().normalize();
                if (!Files.exists(uploadPath)) { Files.createDirectories(uploadPath); }
                
                String originalFileName = imageFile.getOriginalFilename();
                String cleanFileName = originalFileName.replaceAll("\\s+", ""); 
                String fileName = UUID.randomUUID().toString() + "_" + cleanFileName;
                
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                imageWebPath = "/uploads/" + fileName;
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("errorMessage", "画像の保存に失敗しました。");
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
        
        return "redirect:/afterlogin";
    }

    // 4. 投稿の削除処理
    @PostMapping("/prototypes/delete")
    public String deletePrototype(@RequestParam("id") Long id) {
        prototypeRepository.deleteById(id);
        return "redirect:/afterlogin";
    }

    // 5. 編集画面の表示
    @GetMapping("/protos/{id}/edit")
    public String showEditPrototype(@PathVariable("id") Long id, Model model) {
        PrototypeEntity prototype = prototypeRepository.findById(id);
        model.addAttribute("prototype", prototype);
        
        // 🟢 実際のファイルパスに合わせて「prototype_edit」に修正しました
        return "prototype_edit";
    }
// 6. 編集内容の更新保存処理
    // 🟢 @PostMapping の中身を "/protos/{id}/edit" から "/protos/{id}/update" に変更します
    @PostMapping("/protos/{id}/update")
    public String updatePrototype(
            @PathVariable("id") Long id,
            @ModelAttribute("prototype") PrototypeEntity formEntity,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
  
        // 元のデータを取得
        PrototypeEntity prototype = prototypeRepository.findById(id);

        if (prototype != null) {
            prototype.setTitle(formEntity.getTitle());
            prototype.setCatchCopy(formEntity.getCatchCopy());
            prototype.setConcept(formEntity.getConcept());

            // 画像が新しくアップロードされた場合のみ更新
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    Path uploadPath = Paths.get("src/main/resources/static/uploads/").toAbsolutePath().normalize();
                    String originalFileName = imageFile.getOriginalFilename();
                    String cleanFileName = originalFileName.replaceAll("\\s+", ""); 
                    String fileName = UUID.randomUUID().toString() + "_" + cleanFileName;
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    prototype.setImage("/uploads/" + fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            // リポジトリの .update() メソッドを呼び出し
            prototypeRepository.update(prototype);
        }

        // 編集が終わったら詳細ページへリダイレクト
        return "redirect:/prototypes/" + id;
    }
}