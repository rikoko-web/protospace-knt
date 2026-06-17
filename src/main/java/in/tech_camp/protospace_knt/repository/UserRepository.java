package in.tech_camp.protospace_knt.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_knt.entity.UserEntity;

@Mapper
public interface UserRepository {

    // 1. ユーザー登録用
    @Insert("INSERT INTO users (email, password, name, profile, occupation, position) " +
            "VALUES (#{email}, #{password}, #{name}, #{profile}, #{occupation}, #{position})")
    void save(UserEntity user);

    // 2. ログイン照合用
    @Select("SELECT * FROM users WHERE email = #{email}")
    UserEntity findByEmail(String email);

    // 3. ユーザー一覧を取得する場合
    @Select("SELECT * FROM users")
    List<UserEntity> findAll();

    // ★追加：マイページ（詳細画面）を表示するときに、IDからユーザーを特定する用
    @Select("SELECT * FROM users WHERE id = #{id}")
    UserEntity findById(Long id);
}