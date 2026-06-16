package in.tech_camp.protospace_knt.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserForm {

    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "正しいメール形式で入力してください")
    private String email;

    @NotBlank(message = "パスワードは必須です")
    @Size(min = 6, message = "パスワードは6文字以上で入力してください")
    private String password;

    @NotBlank(message = "パスワード（確認用）は必須です")
    private String passwordConfirmation;

    // 👇 DBeaverの「name」に合わせる
    @NotBlank(message = "名前は必須です")
    private String name;

    // 👇 DBeaverの「profile」に合わせる
    @NotBlank(message = "プロフィールは必須です")
    private String profile;

    // 👇 DBeaverの「occupation」に合わせる
    @NotBlank(message = "所属は必須です")
    private String occupation;

    // 👇 DBeaverの「position」に合わせる
    @NotBlank(message = "役職は必須です")
    private String position;
}