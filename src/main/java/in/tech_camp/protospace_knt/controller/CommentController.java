package in.tech_camp.protospace_knt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_knt.entity.CommentEntity;
import in.tech_camp.protospace_knt.form.CommentForm;
import in.tech_camp.protospace_knt.repository.CommentRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;

    // HTMLの「/protos/{id}/comments」に合わせて調整
    @PostMapping("/protos/{prototypeId}/comments")
    public String createComment(@PathVariable("prototypeId") Long prototypeId,
                                @Validated @ModelAttribute("commentForm") CommentForm commentForm,
                                BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "redirect:/prototypes/" + prototypeId;
        }

        CommentEntity comment = new CommentEntity();
        comment.setCommentText(commentForm.getCommentText());
        comment.setPrototypeId(prototypeId);
        
        // 暫定として、投稿ユーザーIDを1番（eriさん）に設定
        comment.setUserId(1L); 

        commentRepository.save(comment);

        // 保存したら、元の詳細ページ（/prototypes/{id}）に戻す
        return "redirect:/prototypes/" + prototypeId;
    }
}