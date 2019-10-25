package com.mdq.yyjhservice.dao.auth;

import com.mdq.yyjhservice.domain.auth.TUserRrole;
import com.mdq.yyjhservice.pojo.MyTUserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TUserRroleMapper {
    int insert(TUserRrole record);
    //根据role_id获取sum
    Integer getSumByRoleId(int role_id);
    //selectUserIdByLimit
    List<Integer> selectUserIdByLimit(@Param("role_id") int role_id
                                    ,@Param("pageSize") int pageSize
                                    ,@Param("begin")    int begin);
    //根据role_id获取用户信息
    List<Integer> selectUserIdByRoleId(int role_id);
    //根据loginid获取用户角色信息
    List<Integer> selectRolesByLoginid(String loginid);
    //根据userId获取用户角色信息
    List<Integer> selectRolesByUserId(int userId);
    //获取整张表（返回MyTUserRole）
    List<MyTUserRole> selectMyAll();
    //根据user_ids获取整行（返回MyTUserRole）
    List<MyTUserRole> selectMyAllByUserIds(@Param("user_ids") List<Integer> user_ids);
    //根据UserId,role_id删除
    //datas参数：int userId , List<Integer> role_ids
    int deleteSomeByUserId(Map<Object,Object> datas);
    //根据user_id,role_id删除一行
    int deleteOneByRoleUser(@Param("role_id") int role_id,@Param("user_id") int user_id);
    //根据user_id删除多行
    int deleteAllByUserId(int user_id);
    //根据loginid删除多行
    int deleteAllByLoginId(String loginId);
    //根据user_ids删除多行
    int deleteSomeByURId(@Param("user_ids") List<Integer> user_ids ,@Param("role_id") int role_id);
    //根据UserId,role_id添加
    //datas参数：int userId , List<Integer> role_ids
    int insertSomeByUserId(Map<Object,Object> datas);
}