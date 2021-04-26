package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userid}")
    List<Credential> getCredentials(int userid);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialid} AND userid = #{userid}")
    Credential getCredential(int credentialid, int userid);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES(#{url}, #{username}, #{key}, #{password}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    int insert(Credential credential);

    @Update("UPDATE CREDENTIALS SET URL = #{url}, USERNAME = #{username}, PASSWORD = #{password} WHERE CREDENTIALID = #{credentialid} AND USERID = #{userid}")
    int update(String url, String username, String password, int credentialid, int userid);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialid} AND userid = #{userid}")
    int delete(int credentialid, int userid);
}
