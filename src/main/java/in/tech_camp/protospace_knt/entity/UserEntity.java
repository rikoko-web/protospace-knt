package in.tech_camp.protospace_knt.entity;

import lombok.Data;

@Data
public class UserEntity {

    private Integer id;
    private String email;
    private String password;
    private String name;
    private String profile;
    private String occupation;
    private String position;
}