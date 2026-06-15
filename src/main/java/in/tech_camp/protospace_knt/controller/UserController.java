package in.tech_camp.protospace_knt.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import in.tech_camp.protospace_knt.entity.UserEntity;       
import in.tech_camp.protospace_knt.repository.UserRepository; 
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class UserController {

    // データベース操作用のリポジトリをインジェクション（注入）します
    private final UserRepository prototypeRepository;

    @GetMapping("/")
    public String index(Model model) {
        // 1. データベースからプロトタイプ（作品）の一覧をすべて取得
        List<UserEntity> prototypes = prototypeRepository.findAll();
        
        // 2. 取得した一覧を「prototypes」という名前で画面（HTML）に渡す
        model.addAttribute("prototypes", prototypes);
        
        // 3. 表示するHTMLを指定：src/main/resources/templates/prototypes/index.html
        return "messages/index";
    }
}
