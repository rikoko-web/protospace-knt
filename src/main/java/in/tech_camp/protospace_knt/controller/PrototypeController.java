package in.tech_camp.protospace_knt.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
        return "prototypes/new"; // templates/prototypes/new.html が必要です
    }

    // 投稿内容の保存（POST受け取り）
    @PostMapping("/protos/new")
    public String createPrototype(@ModelAttribute("prototypeForm") PrototypeForm prototypeForm, 
                                  Authentication auth) {
        
        // ログイン中のユーザー情報を取得
        String email = auth.getName();
        
        // 【今後ここに追加】
        // 1. Service層を使ってDBに保存する処理
        // 2. prototypeFormとemail（ユーザーID）を渡して保存
        
        System.out.println("タイトル: " + prototypeForm.getTitle());
        System.out.println("投稿者: " + email);
        
        return "redirect:/"; // 保存後はトップページへリダイレクト
    }
}