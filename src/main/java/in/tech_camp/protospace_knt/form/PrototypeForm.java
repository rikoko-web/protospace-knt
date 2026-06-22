package in.tech_camp.protospace_knt.form;

import jakarta.validation.constraints.NotBlank; // ★これをついか！
import lombok.Data;

@Data
public class PrototypeForm {

    @NotBlank(message = "タイトルを入力してください") // ★これをついか！
    private String title;

    @NotBlank(message = "キャッチコピーを入力してください") // ★これをついか！
    private String catchCopy;

    @NotBlank(message = "コンセプトを入力してください") // ★これをついか！
    private String concept;

    private String image;
}