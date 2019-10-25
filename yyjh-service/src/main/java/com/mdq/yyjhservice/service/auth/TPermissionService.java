package com.mdq.yyjhservice.service.auth;

import com.mdq.yyjhservice.domain.auth.TPermission;
import com.mdq.yyjhservice.domain.auth.TRole;

import java.util.List;

public interface TPermissionService {
    boolean insert(TPermission record);
    //搜索整张表sum
    Integer getSum();
    //查询整张表
    List<TPermission> selectAll();
    //分页查询
    List<TPermission> selectLimit(String text,int pageSize, int begin);
    //根据roleid获取权限所有信息
    List<TPermission> selectAllByRoleid(int role_id);
    //根据id获取多行
    List<TPermission> selectAllByIdsText(List<Integer> ids,String text);
    //根据role_id获取permissions
    List<String> selectPermissionsByRoleId(int role_id);
    //根据id修改state
    boolean updateStateById(int id,int state);
    //根据id删除一条数据
    boolean deleteOneById(int id);
    //根据ids删除多条数据
    boolean deleteSomeByIds(List<Integer> ids);
}
