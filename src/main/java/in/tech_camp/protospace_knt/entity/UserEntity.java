package in.tech_camp.protospace_knt.entity;

// 💡 エラーの原因だった jakarta.persistence は削除しました！
import jakarta.validation.constraints.Email; // バリデーション用（これは残します！）
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
// 💡 @Entity や @Table(name = "users") はMyBatisでは不要なので削除しました！
public class UserEntity {

    // 💡 @Id や @GeneratedValue は削除しました
    private Integer id;

    // 💡 @NotBlank などのバリデーションは、MyBatisでもそのまま大活躍するので残します！
    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "正しいメール形式で入力してください")
    private String email;

    @NotBlank(message = "パスワードは必須です")
    @Size(min = 6, message = "パスワードは6文字以上で入力してください")
    private String password;

    @NotBlank(message = "名前は必須です")
    private String name;

    @NotBlank(message = "プロフィールは必須です")
    private String profile;

    @NotBlank(message = "所属は必須です")
    private String occupation;

    @NotBlank(message = "役職は必須です")
    private String position;
}