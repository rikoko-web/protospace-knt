package in.tech_camp.protospace_knt.controller;

import org.springframework.security.core.Authentication; // ★指示：Spring SecurityのAuthenticationオブジェクト用
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_knt.entity.CommentEntity;
import in.tech_camp.protospace_knt.entity.UserEntity;
import in.tech_camp.protospace_knt.form.CommentForm;
import in.tech_camp.protospace_knt.repository.CommentRepository;
import in.tech_camp.protospace_knt.repository.UserRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository; 

    // 指示①：「今ログインしているユーザーは誰か」を受け取る引数（Authentication）を追加
    @PostMapping("/protos/{prototypeId}/comments")
    public String createComment(@PathVariable("prototypeId") Long prototypeId,
                                @Validated @ModelAttribute("commentForm") CommentForm commentForm,
                                BindingResult bindingResult,
                                Authentication authentication) { // ←ここに追加しました！

        if (bindingResult.hasErrors()) {
            return "redirect:/protos/" + prototypeId;
        }

        CommentEntity comment = new CommentEntity();
        comment.setCommentText(commentForm.getCommentText());
        comment.setPrototypeId(prototypeId);
        
        // -----------------------------------------------------------------
        // 指示②：Authentication オブジェクトからログイン中ユーザーのメールアドレスを取得
        String email = authentication.getName();
        
        // 指示③：そのメールアドレスからユーザーを検索
        UserEntity user = userRepository.findByEmail(email);
        
        // 指示④：そのユーザーの ID をコメントにセット（型エラー対策で .longValue() を使用）
        comment.setUserId(user.getId().longValue()); 
        // -----------------------------------------------------------------

        commentRepository.save(comment);

        return "redirect:/protos/" + prototypeId;
    }
}