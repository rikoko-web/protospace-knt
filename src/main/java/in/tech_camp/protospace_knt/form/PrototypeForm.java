package in.tech_camp.protospace_knt.form;

import lombok.Data;

@Data
public class PrototypeForm {
    private String title;
    private String catchCopy;
    private String concept;
    private String image; // 💡 追加：画像パス（またはファイル名）を保持する場所
}