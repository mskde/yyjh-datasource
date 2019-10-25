package com.mdq.yyjhservice.dao.auth;

import com.mdq.yyjhservice.domain.auth.TRole;
import com.mdq.yyjhservice.domain.user.TUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TRoleMapper {
    int insert(TRole record);
    //搜索整张表sum
    Integer getSum();
    //查询整张表
    List<TRole> selectAll();
    //分页查询
    List<TRole> selectLimit(@Param("text") String text , @Param("pageSize") int pageSize, @Param("begin") int begin);
    //根据loginId获取用户所有角色
    List<TRole> selectRolenamesByLoginid(String loginId);
    //根据id修改state
    int updateStateById(@Param("id") int id, @Param("state") int state);
    //根据id删除一条数据
    int deleteOneById(int id);
    //根据ids删除多条数据
    int deleteSomeByIds(@Param("ids") List<Integer> ids);
}