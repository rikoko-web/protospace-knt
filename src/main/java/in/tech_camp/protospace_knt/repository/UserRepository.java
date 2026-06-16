package in.tech_camp.protospace_knt.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_knt.entity.UserEntity;

@Mapper
public interface UserRepository {

    // 1. ユーザー登録用（MyBatisの自動連番挿入に対応）
    @Insert("INSERT INTO users (email, password, name, profile, occupation, position) " +
            "VALUES (#{email}, #{password}, #{name}, #{profile}, #{occupation}, #{position})")
    void save(UserEntity user);

    // 2. ログイン照合用
    @Select("SELECT * FROM users WHERE email = #{email}")
    UserEntity findByEmail(String email);

    // 3. ユーザー一覧を取得する場合（※もし作品一覧なら別のPrototypeEntity等になりますが、型をUserEntityに合わせます）
    @Select("SELECT * FROM users")
    List<UserEntity> findAll();
}