package com.mdq.yyjhservice.pojo;

import lombok.Data;

import java.util.List;

@Data
public class ChangeAuthorityUserRolesData {
    private int id;
    private List<Integer> roles;
}
