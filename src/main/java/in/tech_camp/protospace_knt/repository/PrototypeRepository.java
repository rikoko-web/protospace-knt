package in.tech_camp.protospace_knt.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

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

    // 特定のユーザーの投稿だけを絞り込んで取得するメソッド
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

    // ★★★ 【追加】プロトタイプのIDで1件だけ取得するメソッド ★★★
    @Select("SELECT p.*, u.name as user_name FROM prototypes p " +
            "JOIN users u ON p.user_id = u.id " +
            "WHERE p.id = #{id}") // ここがプロトタイプのID指定になります
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
}