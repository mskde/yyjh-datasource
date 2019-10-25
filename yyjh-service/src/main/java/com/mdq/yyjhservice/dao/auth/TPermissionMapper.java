package com.mdq.yyjhservice.dao.auth;

import com.mdq.yyjhservice.domain.auth.TPermission;
import com.mdq.yyjhservice.domain.auth.TRole;
import com.mdq.yyjhservice.domain.user.TUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TPermissionMapper {
    int insert(TPermission record);
    //搜索整张表sum
    Integer getSum();
    //查询整张表
    List<TPermission> selectAll();
    //分页查询
    List<TPermission> selectLimit(@Param("text") String text,@Param("pageSize") int pageSize, @Param("begin") int begin);
    //根据roleid获取权限所有信息
    List<TPermission> selectAllByRoleid(int role_id);
    //根据id获取多行
    List<TPermission> selectAllByIdsText(@Param("ids") List<Integer> ids,@Param("text") String text);
    //根据role_id获取permissions
    List<String> selectPermissionsByRoleId(int role_id);
    //根据id修改state
    int updateStateById(@Param("id") int id, @Param("state") int state);
    //根据id删除一条数据
    int deleteOneById(int id);
    //根据ids删除多条数据
    int deleteSomeByIds(@Param("ids") List<Integer> ids);
}