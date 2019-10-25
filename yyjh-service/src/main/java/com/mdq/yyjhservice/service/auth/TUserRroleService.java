package com.mdq.yyjhservice.service.auth;

import com.mdq.yyjhservice.domain.auth.TUserRrole;
import com.mdq.yyjhservice.pojo.MyTUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TUserRroleService {
    boolean insert(TUserRrole record);
    //根据role_id获取sum
    Integer getSumByRoleId(int role_id);
    //selectUserIdByLimit
    List<Integer> selectUserIdByLimit(int role_id,int pageSize,int begin);
    //根据loginid获取用户角色信息
    List<Integer> selectRolesByLoginid(String loginid);
    //根据userId获取用户角色信息
    List<Integer> selectRolesByUserId(int userId);
    //根据role_id获取用户信息
    List<Integer> selectUserIdByRoleId(int role_id);
    //获取整张表（返回MyTUserRole）
    List<MyTUserRole> selectMyAll();
    //根据user_ids获取整行（返回MyTUserRole）
    List<MyTUserRole> selectMyAllByUserIds(List<Integer> user_ids);
    //根据userId删除
    //datas参数：int userId , List<Integer> role_ids
    boolean deleteSomeByUserId(Map<Object,Object> datas);
    //根据user_id,role_id删除一行
    boolean deleteOneByRoleUser(int role_id,int user_id);
    //根据user_id删除多行
    boolean deleteAllByUserId(int user_id);
    //根据loginid删除多行
    boolean deleteAllByLoginId(String loginId);
    //根据user_ids删除多行
    boolean deleteSomeByURId(List<Integer> user_ids , int role_id);
    //根据UserId,role_id添加
    //datas参数：int userId , List<Integer> role_ids
    boolean insertSomeByUserId(Map<Object,Object> datas);
}
