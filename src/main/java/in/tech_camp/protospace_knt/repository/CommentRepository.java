package in.tech_camp.protospace_knt.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_knt.entity.CommentEntity;

@Mapper
public interface CommentRepository {

    // コメントを保存する
    @Insert("INSERT INTO comments (comment_text, user_id, prototype_id) VALUES (#{commentText}, #{userId}, #{prototypeId})")
    void save(CommentEntity comment);

    // ★★★ 【修正】usersテーブルとJOINして、コメント投稿者の名前とIDもまとめて取得する ★★★
    @Select("SELECT c.*, u.name as user_name FROM comments c " +
            "JOIN users u ON c.user_id = u.id " +
            "WHERE c.prototype_id = #{prototypeId} ORDER BY c.id DESC")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "commentText", column = "comment_text"),
        @Result(property = "userId", column = "user_id"),
        @Result(property = "prototypeId", column = "prototype_id"),
        @Result(property = "user.name", column = "user_name"), // HTMLの ${comment.user.name} に紐づきます
        @Result(property = "user.id", column = "user_id")      // HTMLの ${comment.user.id} に紐づきます
    })
    List<CommentEntity> findByPrototypeId(Long prototypeId);

    @Delete("DELETE FROM comments WHERE prototype_id = #{prototypeId}")
    void deleteByPrototypeId(Long prototypeId);
}