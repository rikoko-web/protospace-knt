package in.tech_camp.protospace_knt.form;

import lombok.Data;

@Data
public class CommentForm {
    private String commentText; // HTMLの th:field="*{commentText}" と名前を一致させる
}