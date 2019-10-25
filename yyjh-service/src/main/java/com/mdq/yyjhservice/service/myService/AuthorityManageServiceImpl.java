package com.mdq.yyjhservice.service.myService;

import com.mdq.yyjhservice.domain.auth.TRolePermission;
import com.mdq.yyjhservice.enumeration.DatasourceEnum;
import com.mdq.yyjhservice.service.auth.TRolePermissionService;
import com.mdq.yyjhservice.service.auth.TUserRroleService;
import com.mdq.yyjhservice.vo.ControllerResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Primary
@Slf4j
@Transactional
public class AuthorityManageServiceImpl implements AuthorityManageService{
    @Override
    public ControllerResult updateUserRoles(int userId, List<Integer> roles, TUserRroleService tur) {
        ControllerResult result = new ControllerResult();
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        result.setCode(DatasourceEnum.FAIL.getCode());
        //获取用户原始角色信息
        List<Integer> old_roles = tur.selectRolesByUserId(userId);
        List<Integer> copy_old_roles = new ArrayList<Integer>();
        copy_old_roles.addAll(old_roles);
        //删除旧的角色
        Map<Object,Object> delete_datas = new HashMap<Object,Object>();
        old_roles.removeAll(roles);
        List<Integer> delete_roles = old_roles;
        System.out.println("delete_roles:"+delete_roles);
        if(null != delete_roles && delete_roles.size() > 0){
            //存在需要删除的角色
            delete_datas.put("role_ids",delete_roles);
            delete_datas.put("userId",userId);
            //删除
            boolean delete_flag = tur.deleteSomeByUserId(delete_datas);
            if(delete_flag){
                //删除成功
            }
            else{
                //删除失败
                result.setPayload("删除原始角色失败！");
                return result;
            }
        }
        else{
            //没有需要删除的角色
        }
        //添加新角色
        Map<Object,Object> insert_datas = new HashMap<Object,Object>();
        System.out.println("copy_old_roles:"+copy_old_roles);
        roles.removeAll(copy_old_roles);
        List<Integer> insert_roles = roles;
        System.out.println("insert_roles:"+insert_roles);
        if(null != insert_roles && insert_roles.size() > 0){
            //存在需要添加的角色
            insert_datas.put("role_ids",insert_roles);
            insert_datas.put("userId",userId);
            //添加
            boolean insert_flag = tur.insertSomeByUserId(insert_datas);
            if(insert_flag){
                //添加成功
                result.setCode(DatasourceEnum.SUCCESS.getCode());
                result.setMsg(DatasourceEnum.SUCCESS.getMsg());
            }
            else{
                //添加失败
                result.setPayload("添加新角色失败！");
                return result;
            }
        }
        else{
            //不存在需要添加的角色
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        return result;
    }

    @Override
    public ControllerResult updatePermissionRoles(int permissionId, List<Integer> roles, TRolePermissionService trp) {
        ControllerResult result = new ControllerResult();
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        result.setCode(DatasourceEnum.FAIL.getCode());
        //根据permissionId获取映射
        List<TRolePermission> old_trps = trp.selectByPermissionId(permissionId);
        boolean flag = true;
        if(null != old_trps && old_trps.size()>0){
            //删除旧的角色
            flag = trp.deleteByPermissionId(permissionId);
        }
        if(flag){
            //删除成功
            if(null != roles && roles.size() > 0){
                //添加角色
                boolean flag2 = trp.insertSomeByPidRids(permissionId,roles);
                if(flag2) {
                    //添加成功
                    result.setCode(DatasourceEnum.SUCCESS.getCode());
                    result.setMsg(DatasourceEnum.SUCCESS.getMsg());
                }
                else {
                    //添加失败
                    result.setPayload("添加新角色失败！");
                }
            }
           else{
               //执行成功
                result.setCode(DatasourceEnum.SUCCESS.getCode());
                result.setMsg(DatasourceEnum.SUCCESS.getMsg());
            }
        }
        else{
            //删除旧映射失败
            result.setPayload("删除旧角色失败！");
        }
        return result;
    }
}
