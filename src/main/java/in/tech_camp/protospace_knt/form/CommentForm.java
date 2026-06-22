package in.tech_camp.protospace_knt.form;

import jakarta.validation.constraints.NotBlank; // ★追加
import lombok.Data;

@Data
public class CommentForm {
    @NotBlank(message = "コメント内容を入力してください") // ★追加
    private String commentText; // HTMLの th:field="*{commentText}" と名前を一致させる
}