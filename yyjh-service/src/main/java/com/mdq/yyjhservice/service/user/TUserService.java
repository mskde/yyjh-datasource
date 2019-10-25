package com.mdq.yyjhservice.service.user;

import com.mdq.yyjhservice.domain.user.TUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TUserService {
    boolean createTable();
    //注册
    boolean insert(TUser record);
    //搜索整张表sum
    int getSum();
    //查询整张表
    List<TUser> selectAll();
    //分页查询
    List<TUser> selectLimit(String text,int pageSize,int begin);
    //根据账号获取loginId(账号可能是loginId,邮箱，手机号)
    String selectLoginIdByUser(String user);
    //根据账号获取用户所有信息
    TUser selectAllByUser(String loginid);
    //根据账号获取id
    String    getUseridByLoginid(String loginId);
    //根据账号获取密码
    String getPwdByLoginid(String loginId);
    //根据账号获取头像
    String getUserimgByLoginid(String loginId);
    //根据账号获取邮箱
    String getEmailByLoginid(String loginId);
    //根据账号获取邮箱,密码
    Map<Object,String> getEmail_and_PasswordByLoginid(String loginId);
    //根据ids获取用户数据
    List<TUser> getUsersByIdsText(List<Integer> ids,String text);
    //查询账号是否已存在
    boolean isLoginIdRepeat(String loginId);
    //查询账号是否已存在
    boolean isEmailRepeat(String email);
    //查询手机号
    boolean isTelRepeat(String tel);
    //根据账号修改密码
    boolean updatePasswordByLoginid(String loginId,String password);
    //根据id修改state
    boolean updateStateById(int id,int state);
    //根据id删除一行
    boolean deleteOneById(int id);
    //根据loginid删除行
    boolean deleteOneByLoginid(String loginId);
    //根据ids删除多行
    boolean deleteSomeByIds(List<Integer> ids);
    //根据loginids删除多行
    boolean deleteSomeByLoginid(String[] loginIds);
    //根据loginid修改昵称
    boolean updateInfoByLoginId(String loginid , String infoname , String value);
}
