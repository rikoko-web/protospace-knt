package in.tech_camp.protospace_knt.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import in.tech_camp.protospace_knt.entity.PrototypeEntity;

@Mapper
public interface PrototypeRepository {

    @Insert("INSERT INTO prototypes (title, catch_copy, concept, image, user_id) " +
            "VALUES (#{title}, #{catchCopy}, #{concept}, #{image}, #{userId})")
    void insert(PrototypeEntity prototype);

    @Select("SELECT p.*, u.name as user_name FROM prototypes p " +
            "JOIN users u ON p.user_id = u.id")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "title", column = "title"),
        @Result(property = "catchCopy", column = "catch_copy"),
        @Result(property = "concept", column = "concept"),
        @Result(property = "image", column = "image"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "user.name", column = "user_name"),
        @Result(property = "user.id", column = "user_id")
    })
    List<PrototypeEntity> findAll();

    @Select("SELECT p.*, u.name as user_name FROM prototypes p " +
            "JOIN users u ON p.user_id = u.id " +
            "WHERE p.user_id = #{userId}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "title", column = "title"),
        @Result(property = "catchCopy", column = "catch_copy"),
        @Result(property = "concept", column = "concept"),
        @Result(property = "image", column = "image"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "user.name", column = "user_name"),
        @Result(property = "user.id", column = "user_id")
    })
    List<PrototypeEntity> findByUserId(Long userId);

    @Delete("DELETE FROM prototypes WHERE id = #{id}")
    void deleteById(Long id);

    @Select("SELECT p.*, u.name as user_name FROM prototypes p " +
            "JOIN users u ON p.user_id = u.id " +
            "WHERE p.id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "title", column = "title"),
        @Result(property = "catchCopy", column = "catch_copy"),
        @Result(property = "concept", column = "concept"),
        @Result(property = "image", column = "image"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "user.name", column = "user_name"),
        @Result(property = "user.id", column = "user_id")
    })
    PrototypeEntity findById(Long id);

    // 🟢 COALESCEを使用して、imageがnullなら既存の値をそのまま維持するように修正
    @Update("UPDATE prototypes SET title = #{title}, catch_copy = #{catchCopy}, concept = #{concept}, " +
            "image = COALESCE(#{image}, image) WHERE id = #{id}")
    void update(PrototypeEntity prototype);
}