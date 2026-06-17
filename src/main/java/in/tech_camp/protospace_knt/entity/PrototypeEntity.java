package in.tech_camp.protospace_knt.entity;

import java.time.LocalDateTime;

public class PrototypeEntity {
    private Long id;
    private String title;
    private String catchCopy;
    private String concept;
    private String image;
    private LocalDateTime createdAt;
    private Long userId; // ★ここが今回重要です

    // コンストラクタ
    public PrototypeEntity() {}

    public PrototypeEntity(Long id, String title, String catchCopy, String concept, String image, LocalDateTime createdAt, Long userId) {
        this.id = id;
        this.title = title;
        this.catchCopy = catchCopy;
        this.concept = concept;
        this.image = image;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    // GetterとSetter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}