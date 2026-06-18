package in.tech_camp.protospace_knt.entity;

import java.time.LocalDateTime;

public class PrototypeEntity {
    private Integer id;
    private String title;
    private String catchCopy;
    private String concept;
    private String image;
    private LocalDateTime createdAt;
    private Integer userId; 
    
    // ★MyBatisの場合、関連付けはJava側ではなくSQL側（Mapper）で行うのが一般的です
    private UserEntity user;

    // コンストラクタ
    public PrototypeEntity() {}

    // GetterとSetter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCatchCopy() { return catchCopy; }
    public void setCatchCopy(String catchCopy) { this.catchCopy = catchCopy; }

    public String getConcept() { return concept; }
    public void setConcept(String concept) { this.concept = concept; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public UserEntity getUser() { return user; }
    public void setUser(UserEntity user) { this.user = user; }
}