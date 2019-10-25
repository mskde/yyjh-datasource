package com.mdq.yyjhservice.service.auth;

import com.mdq.yyjhservice.dao.auth.TRolePermissionMapper;
import com.mdq.yyjhservice.domain.auth.TRolePermission;
import com.mdq.yyjhservice.pojo.MyTRolePermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Primary
@Transactional
@Slf4j
public class TRolePermissionServiceImpl implements TRolePermissionService{
    @Autowired
    private TRolePermissionMapper trp;

    @Override
    public int insert(TRolePermission record) {
        return trp.insert(record);
    }

    @Override
    public boolean insertOneByRP(int role_id, String permission) {
        return trp.insertOneByRP(role_id,permission) > 0 ? true : false;
    }

    @Override
    public boolean insertSomeByPidRids(int permission_id, List<Integer> role_ids) {
        return trp.insertSomeByPidRids(permission_id,role_ids) > 0 ? true : false;
    }

    @Override
    public Integer getSumByRoleId(int role_id) {
        return trp.getSumByRoleId(role_id);
    }

    @Override
    public List<Integer> selectPermIdsByLimit(int role_id, int pageSize, int begin) {
        return trp.selectPermIdsByLimit(role_id,pageSize,begin);
    }

    @Override
    public List<TRolePermission> selectByPermissionId(int permissionId) {
        return trp.selectByPermissionId(permissionId);
    }

    @Override
    public List<Integer> selectPermission_idByRole_id(List<Integer> roles) {
        return trp.selectPermission_idByRole_id(roles);
    }

    @Override
    public List<Integer> selectPermIdsByRoleId(int role_id) {
        return trp.selectPermIdsByRoleId(role_id);
    }

    @Override
    public List<MyTRolePermission> selectMyAll() {
        return trp.selectMyAll();
    }

    @Override
    public List<MyTRolePermission> selectRolesByPermIds(List<Integer> permIds) {
        return trp.selectRolesByPermIds(permIds);
    }

    @Override
    public TRolePermission selectOneByRP(int role_id, String permission) {
        return trp.selectOneByRP(role_id,permission);
    }

    @Override
    public boolean deleteByPermissionId(int permission_id) {
        return trp.deleteByPermissionId(permission_id) > 0 ? true : false;
    }

    @Override
    public boolean deleteOneByRolePermission(int role_id, int permission_id) {
        return trp.deleteOneByRolePermission(role_id,permission_id) > 0 ? true : false;
    }

    @Override
    public boolean deleteSomeByPRId(List<Integer> permission_ids ,int role_id) {
        return trp.deleteSomeByPRId(permission_ids,role_id) > 0 ? true : false;
    }
}
