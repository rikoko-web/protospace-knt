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
import org.springframework.web.multipart.MultipartFile; // 💡 これをインポートします

import in.tech_camp.protospace_knt.form.PrototypeForm;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PrototypeController {

    // private final PrototypeService prototypeService; // 今後ここを有効にします

    // 投稿画面の表示
    @GetMapping("/protos/new")
    public String showNewPrototype(Model model) {
        model.addAttribute("prototypeForm", new PrototypeForm());
        return "protos/new"; // 💡HTMLファイルの場所に合わせて templates/protos/new.html を指定
    }

    // 投稿内容の保存（POST受け取り）
    @PostMapping("/protos/new")
    public String createPrototype(
            @ModelAttribute("prototypeForm") PrototypeForm prototypeForm, 
            @RequestParam("imageFile") MultipartFile imageFile, // 💡 変更点1：画像ファイルを受け取る引数を追加
            Authentication auth) {
        
        // ログイン中のユーザー情報を取得
        String email = auth.getName();
        
        // 💡 変更点2：画像の保存処理
        String imageWebPath = null;
        if (!imageFile.isEmpty()) {
            try {
                // 保存先フォルダ（src/main/resources/static/uploads/）のパスを指定
                Path uploadPath = Paths.get("src/main/resources/static/uploads/").toAbsolutePath().normalize();
                
                // フォルダが存在しなければ作成する
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // ファイル名の重複を防ぐため、ランダムな文字列（UUID）をファイル名の頭につける
                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

                // 実際に指定フォルダへファイルを保存（書き出し）する
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // データベースに保存するためのURLパス（文字列）を作成
                imageWebPath = "/uploads/" + fileName;

            } catch (IOException e) {
                e.printStackTrace();
                // エラーが起きた場合は保存画面に戻す
                return "protos/new";
            }
        }

        // 💡 【今後ここに追加する部分】
        // 1. 取得した imageWebPath（画像の住所文字列）を prototypeForm や Service にセットする
        // prototypeForm.setImage(imageWebPath); など（Form側にフィールドがある場合）
        // prototypeService.save(prototypeForm, email);
        
        System.out.println("タイトル: " + prototypeForm.getTitle());
        System.out.println("投稿者: " + email);
        System.out.println("画像の保存先パス: " + imageWebPath); // 💡 コンソールで確認できるように追加
        
        return "redirect:/"; // 保存後はトップページへリダイレクト
    }
}