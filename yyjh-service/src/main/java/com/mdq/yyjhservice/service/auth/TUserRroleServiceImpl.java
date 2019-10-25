package com.mdq.yyjhservice.service.auth;

import com.mdq.yyjhservice.dao.auth.TUserRroleMapper;
import com.mdq.yyjhservice.domain.auth.TUserRrole;
import com.mdq.yyjhservice.pojo.MyTUserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Primary
@Slf4j
@Transactional
public class TUserRroleServiceImpl implements TUserRroleService{
    @Autowired
    private TUserRroleMapper tur;
    @Override
    public boolean insert(TUserRrole record) {
        return tur.insert(record)>0?true:false;
    }

    @Override
    public Integer getSumByRoleId(int role_id) {
        return tur.getSumByRoleId(role_id);
    }

    @Override
    public List<Integer> selectUserIdByLimit(int role_id, int pageSize, int begin) {
        return tur.selectUserIdByLimit(role_id,pageSize,begin);
    }

    @Override
    public List<Integer> selectRolesByLoginid(String loginid) {
        return tur.selectRolesByLoginid(loginid);
    }

    @Override
    public List<Integer> selectRolesByUserId(int userId) {
        return tur.selectRolesByUserId(userId);
    }

    @Override
    public List<Integer> selectUserIdByRoleId(int role_id) {
        return tur.selectUserIdByRoleId(role_id);
    }

    @Override
    public List<MyTUserRole> selectMyAll() { return tur.selectMyAll(); }

    @Override
    public List<MyTUserRole> selectMyAllByUserIds(List<Integer> user_ids) {
        return tur.selectMyAllByUserIds(user_ids);
    }

    @Override
    public boolean deleteSomeByUserId(Map<Object, Object> datas) {
        return tur.deleteSomeByUserId(datas) > 0 ? true : false;
    }

    @Override
    public boolean deleteOneByRoleUser(int role_id, int user_id) {
        return tur.deleteOneByRoleUser(role_id,user_id) > 0 ? true : false;
    }

    @Override
    public boolean deleteAllByUserId(int user_id) {
        return tur.deleteAllByUserId(user_id) > 0 ? true : false;
    }

    @Override
    public boolean deleteAllByLoginId(String loginId) {
        return tur.deleteAllByLoginId(loginId) > 0 ? true : false;
    }

    @Override
    public boolean deleteSomeByURId(List<Integer> user_ids,int role_id) {
        return tur.deleteSomeByURId(user_ids,role_id) > 0 ? true : false;
    }

    @Override
    public boolean insertSomeByUserId(Map<Object, Object> datas) {
        return tur.insertSomeByUserId(datas) > 0 ?true : false;
    }
}
