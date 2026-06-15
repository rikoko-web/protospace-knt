package in.tech_camp.protospace_knt.repository;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import in.tech_camp.protospace_knt.entity.UserEntity;

@Mapper
public interface UserRepository {

    // 1. ユーザーを新しく登録する（保存する）ためのSQL
    @Insert("INSERT INTO users (email, password, name, profile, occupation, position) VALUES (#{email}, #{password}, #{name}, #{profile}, #{occupation}, #{position})")
    void save(UserEntity user);

    // 2. ログインや重複チェックの時、メールアドレスでユーザーを探すためのSQL
    @Select("SELECT * FROM users WHERE email = #{email}")
    UserEntity findByEmail(String email);

    @Select("SELECT * FROM users")
    List<UserEntity> findAll();
}