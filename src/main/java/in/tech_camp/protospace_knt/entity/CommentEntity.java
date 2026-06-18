package in.tech_camp.protospace_knt.entity;

import lombok.Data;

@Data
public class CommentEntity {
    private Long id;
    private String commentText;
    private Long userId;        // 誰が書いたか
    private Long prototypeId;   // どのプロトタイプに対するコメントか
    
    // ★★★ 【追加】コメントしたユーザーの情報を丸ごと保持するフィールド ★★★
    private UserEntity user;
}