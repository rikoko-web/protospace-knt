package in.tech_camp.protospace_knt.controller;

import org.springframework.security.core.Authentication;
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

    @PostMapping("/protos/{prototypeId}/comments")
    public String createComment(@PathVariable("prototypeId") Long prototypeId,
                                @Validated @ModelAttribute("commentForm") CommentForm commentForm,
                                BindingResult bindingResult,
                                Authentication authentication) {

        if (bindingResult.hasErrors()) {
            return "redirect:/protos/" + prototypeId;
        }

        CommentEntity comment = new CommentEntity();
        comment.setCommentText(commentForm.getCommentText());
        comment.setPrototypeId(prototypeId);

        String email = authentication.getName();
        UserEntity user = userRepository.findByEmail(email);
        comment.setUserId(user.getId().longValue());

        commentRepository.save(comment);

        return "redirect:/protos/" + prototypeId;
    }
}