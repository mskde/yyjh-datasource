package com.mdq.yyjhservice.dao.auth;

import com.mdq.yyjhservice.domain.auth.TRolePermission;
import com.mdq.yyjhservice.pojo.MyTRolePermission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TRolePermissionMapper {
    int insert(TRolePermission record);
    //根据role_id,permission添加一条数据
    int insertOneByRP(@Param("role_id") int role_id,@Param("permission") String permission);
    //根据permission_id,role_ids添加多行
    int insertSomeByPidRids(@Param("permission_id") int permission_id , @Param("role_ids") List<Integer> role_ids);
    //根据role_id获取sum
    Integer getSumByRoleId(int role_id);
    //分页查询
    List<Integer> selectPermIdsByLimit(@Param("role_id") int role_id
                                    ,@Param("pageSize") int pageSize
                                    ,@Param("begin")    int begin);
    //根据permission_id搜索
    List<TRolePermission> selectByPermissionId(int permissionId);
    //根据loginid获取账号权限信息
    List<Integer> selectPermission_idByRole_id(@Param("roles") List<Integer> roles);
    //根据role_id获取permission_id
    List<Integer> selectPermIdsByRoleId(int role_id);
    //搜索整张带rolename的t_role_permission表
    List<MyTRolePermission> selectMyAll();
    //根据perm_ids获取MyTRolePermission
    List<MyTRolePermission> selectRolesByPermIds (@Param("permIds") List<Integer> permIds);
    //查询映射是否存在
    //根据role_id,permission查询
    TRolePermission selectOneByRP(@Param("role_id") int role_id,@Param("permission") String permission);
    //根据permission_id删除
    int deleteByPermissionId(int permission_id);
    //根据role_id,permission_id删除一行
    int deleteOneByRolePermission(@Param("role_id") int role_id, @Param("permission_id")int permission_id);
    //根据permission_ids删除多行
    int deleteSomeByPRId(@Param("permission_ids") List<Integer> permission_ids ,@Param("role_id") int role_id);
}