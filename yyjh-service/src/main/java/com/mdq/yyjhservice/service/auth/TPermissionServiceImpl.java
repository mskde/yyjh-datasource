package com.mdq.yyjhservice.service.auth;

import com.mdq.yyjhservice.dao.auth.TPermissionMapper;
import com.mdq.yyjhservice.domain.auth.TPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Primary
@Slf4j
@Transactional
public class TPermissionServiceImpl implements TPermissionService {
    @Autowired
    private TPermissionMapper tp;

    @Override
    public boolean insert(TPermission record) {
        int count = tp.insert(record);
        return count>0?true:false;
    }

    @Override
    public Integer getSum() {
        return tp.getSum();
    }

    @Override
    public List<TPermission> selectAll() {
        return tp.selectAll();
    }

    @Override
    public List<TPermission> selectLimit(String text , int pageSize, int begin) {
        return tp.selectLimit(text,pageSize,begin);
    }

    @Override
    public List<TPermission> selectAllByRoleid(int role_id) {
        return tp.selectAllByRoleid(role_id);
    }

    @Override
    public List<TPermission> selectAllByIdsText(List<Integer> ids,String text) {
        return tp.selectAllByIdsText(ids,text);
    }

    @Override
    public List<String> selectPermissionsByRoleId(int role_id) {
        return tp.selectPermissionsByRoleId(role_id);
    }

    @Override
    public boolean updateStateById(int id, int state) {
        return tp.updateStateById(id,state) >0 ? true : false;
    }

    @Override
    public boolean deleteOneById(int id) {
        return tp.deleteOneById(id) > 0 ? true : false;
    }

    @Override
    public boolean deleteSomeByIds(List<Integer> ids) {
        return tp.deleteSomeByIds(ids) > 0 ? true : false;
    }
}
