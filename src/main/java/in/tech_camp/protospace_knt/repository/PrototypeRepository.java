package in.tech_camp.protospace_knt.repository;

import java.util.List; // ★これが必要

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result; // ★これが必要
import org.apache.ibatis.annotations.Results; // ★これが必要
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
}