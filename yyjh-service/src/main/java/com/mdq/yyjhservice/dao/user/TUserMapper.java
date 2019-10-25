package com.mdq.yyjhservice.dao.user;

import com.mdq.yyjhservice.domain.user.TUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TUserMapper {
    boolean createTable();
    //注册
    int insert(TUser record);
    //搜索整张表sum
    int getSum();
    //查询整张表
    List<TUser> selectAll();
    //分页查询
    List<TUser> selectLimit(@Param("text") String text,@Param("pageSize") int pageSize,@Param("begin") int begin);
    //根据账号获取loginId(账号可能是loginId,邮箱，手机号)
    String selectLoginIdByUser(String user);
    //根据账号获取用户所有信息
    TUser selectAllByUser(String loginid);
    //根据账号获取id
    String getUseridByLoginid(String loginId);
    //根据账号获取密码
    String getPwdByLoginid(String loginId);
    //根据账号获取头像
    String getUserimgByLoginid(String loginId);
    //根据账号获取邮箱
    String getEmailByLoginid(String loginId);
    //根据账号获取邮箱,密码
    Map<Object,String> getEmail_and_PasswordByLoginid(String loginId);
    //根据ids获取用户数据
    List<TUser> getUsersByIdsText(@Param("ids") List<Integer> ids , @Param("text") String text);
    //查询账号是否已存在
    int isLoginIdRepeat(String loginId);
    //查询邮箱是否已存在
    int isEmailRepeat(String email);
    //查询手机号
    int isTelRepeat(String tel);
    //根据账号修改密码
    int updatePasswordByLoginid(@Param("loginId") String loginId , @Param("password") String password);
    //根据id修改state
    int updateStateById(@Param("id") int id,@Param("state") int state);
    //根据id删除一行
    int deleteOneById(int id);
    //根据loginid删除一行
    int deleteOneByLoginid(String loginId);
    //根据ids删除多行
    int deleteSomeByIds(@Param("ids") List<Integer> ids);
    //根据loginids删除多行
    int deleteSomeByLoginid(@Param("loginIds") String[] loginIds);
    //根据loginid修改昵称
    int updateInfoByLoginId(@Param("loginid") String loginid , @Param("infoname")String infoname , @Param("value")String value);
}