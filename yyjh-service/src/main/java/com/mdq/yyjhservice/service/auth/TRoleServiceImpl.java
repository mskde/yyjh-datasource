package com.mdq.yyjhservice.service.auth;

import com.mdq.yyjhservice.dao.auth.TRoleMapper;
import com.mdq.yyjhservice.domain.auth.TRole;
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
public class TRoleServiceImpl implements TRoleService{
    @Autowired
    private TRoleMapper tr;

    @Override
    public boolean insert(TRole record) {
        int count = tr.insert(record);
        return count>0?true:false;
    }

    @Override
    public Integer getSum() {
        return tr.getSum();
    }

    @Override
    public List<TRole> selectAll() {
        return tr.selectAll();
    }

    @Override
    public List<TRole> selectLimit(String text,int pageSize, int begin) {
        return tr.selectLimit(text,pageSize,begin);
    }

    @Override
    public List<TRole> selectRolenamesByLoginid(String loginId) {
        return tr.selectRolenamesByLoginid(loginId);
    }

    @Override
    public boolean updateStateById(int id, int state) {
        return tr.updateStateById(id,state) >0 ? true : false;
    }

    @Override
    public boolean deleteOneById(int id) {
        return tr.deleteOneById(id) > 0 ? true :false;
    }

    @Override
    public boolean deleteSomeByIds(List<Integer> ids) {
        return tr.deleteSomeByIds(ids) > 0 ? true : false;
    }
}
