package com.mdq.yyjhservice.pojo;

import lombok.Data;

@Data
public class MyTRolePermission {
    private Integer id;

    private Integer roleId;

    private Integer permissionId;

    private String roleName;
}
