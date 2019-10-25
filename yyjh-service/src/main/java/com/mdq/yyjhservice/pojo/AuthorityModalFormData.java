package com.mdq.yyjhservice.pojo;

import lombok.Data;

@Data
public class AuthorityModalFormData {
    private String loginid;
    private int auth_insert;
    private int auth_delete;
    private int auth_update;
    private int auth_select;
    private int auth_authManager;
    private int auth_dateFormat;
    private int auth_userDelete;
}
