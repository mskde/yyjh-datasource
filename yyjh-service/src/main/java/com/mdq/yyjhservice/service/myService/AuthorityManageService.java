package com.mdq.yyjhservice.service.myService;

import com.mdq.yyjhservice.domain.auth.TRolePermission;
import com.mdq.yyjhservice.service.auth.TRolePermissionService;
import com.mdq.yyjhservice.service.auth.TUserRroleService;
import com.mdq.yyjhservice.vo.ControllerResult;

import java.util.List;

public interface AuthorityManageService {
    //更新用户角色
    ControllerResult updateUserRoles(int userId, List<Integer>roles , TUserRroleService tur);
    //更新权限角色
    ControllerResult updatePermissionRoles(int permissionId, List<Integer>roles , TRolePermissionService tur);
}
