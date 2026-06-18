package in.tech_camp.protospace_knt.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import in.tech_camp.protospace_knt.entity.CommentEntity;

@Mapper
public interface CommentRepository {

    // コメントを保存する
    @Insert("INSERT INTO comments (comment_text, user_id, prototype_id) VALUES (#{commentText}, #{userId}, #{prototypeId})")
    void save(CommentEntity comment);

    // 特定のプロトタイプに紐づくコメントを全件取得する
    @Select("SELECT * FROM comments WHERE prototype_id = #{prototypeId} ORDER BY id DESC")
    List<CommentEntity> findByPrototypeId(Long prototypeId);
}