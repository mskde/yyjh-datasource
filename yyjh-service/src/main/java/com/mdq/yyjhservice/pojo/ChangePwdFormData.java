package com.mdq.yyjhservice.pojo;

import lombok.Data;

@Data
public class ChangePwdFormData {
    private String loginId;
    private String oldpassword;
    private String newpassword;
    private String email;
    private String code;
    private String recode;
}
