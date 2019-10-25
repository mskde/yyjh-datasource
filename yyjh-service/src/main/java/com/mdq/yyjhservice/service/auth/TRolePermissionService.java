package com.mdq.yyjhservice.service.auth;

import com.mdq.yyjhservice.domain.auth.TRolePermission;
import com.mdq.yyjhservice.pojo.MyTRolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TRolePermissionService {
    int insert(TRolePermission record);
    //根据role_id,permission添加一条数据
    boolean insertOneByRP(int role_id,String permission);
    //根据permission_id,role_ids添加多行
    boolean insertSomeByPidRids(int permission_id ,List<Integer> role_ids);
    //根据role_id获取sum
    Integer getSumByRoleId(int role_id);
    //selectUserIdByLimit
    List<Integer> selectPermIdsByLimit(int role_id,int pageSize,int begin);
    //根据permission_id搜索
    List<TRolePermission> selectByPermissionId(int permissionId);
    //根据loginid获取账号权限信息
    List<Integer> selectPermission_idByRole_id(List<Integer> roles);
    //根据role_id获取permission_id
    List<Integer> selectPermIdsByRoleId(int role_id);
    //搜索整张带rolename的t_role_permission表
    List<MyTRolePermission> selectMyAll();
    //根据perm_ids获取MyTRolePermission
    List<MyTRolePermission> selectRolesByPermIds(List<Integer> permIds);
    //查询映射是否存在
    //根据role_id,permission查询
    TRolePermission selectOneByRP(int role_id,String permission);
    //根据permission_id删除
    boolean deleteByPermissionId(int permission_id);
    //根据role_id,permission_id删除一行
    boolean deleteOneByRolePermission(int role_id,int permission_id);
    //根据permission_ids删除多行
    boolean deleteSomeByPRId(List<Integer> permission_ids , int role_id);
}
