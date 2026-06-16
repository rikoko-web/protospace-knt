package in.tech_camp.protospace_knt.entity;

import jakarta.persistence.*; // DB連携用
import jakarta.validation.constraints.*; // バリデーション用
import lombok.Data;

@Entity // これにより、このクラスがDBテーブルと紐づきます
@Table(name = "users") // SQLの users テーブルを指します
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 「必須」「メール形式」「一意性(DB側)」の要件
    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "正しいメール形式で入力してください")
    @Column(nullable = false, unique = true)
    private String email;

    // 「必須」「6文字以上」の要件
    @NotBlank(message = "パスワードは必須です")
    @Size(min = 6, message = "パスワードは6文字以上で入力してください")
    @Column(nullable = false)
    private String password;

    // 「必須」の要件
    @NotBlank(message = "名前は必須です")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "プロフィールは必須です")
    @Column(nullable = false)
    private String profile;

    @NotBlank(message = "所属は必須です")
    @Column(nullable = false)
    private String occupation;

    @NotBlank(message = "役職は必須です")
    @Column(nullable = false)
    private String position;
}