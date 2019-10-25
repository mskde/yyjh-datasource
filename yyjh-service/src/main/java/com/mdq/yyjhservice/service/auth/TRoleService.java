package com.mdq.yyjhservice.service.auth;

import com.mdq.yyjhservice.domain.auth.TRole;
import com.mdq.yyjhservice.domain.user.TUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TRoleService {
    boolean insert(com.mdq.yyjhservice.domain.auth.TRole record);
    //搜索整张表sum
    Integer getSum();
    //查询整张表
    List<TRole> selectAll();
    //分页查询
    List<TRole> selectLimit(String text,int pageSize,int begin);
    //根据loginId获取用户所有角色
    List<TRole> selectRolenamesByLoginid(String loginId);
    //根据id修改state
    boolean updateStateById(int id,int state);
    //根据id删除一条数据
    boolean deleteOneById(int id);
    //根据ids删除多条数据
    boolean deleteSomeByIds(List<Integer> ids);
}
