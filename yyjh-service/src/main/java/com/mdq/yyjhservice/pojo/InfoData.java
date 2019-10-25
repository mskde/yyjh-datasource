package com.mdq.yyjhservice.pojo;

import lombok.Data;
import java.util.List;

@Data
public class InfoData {
    private Integer id;

    private String loginid;

    private String nickname;

    private String userimg;

    private String email;

    private String tel;

    private String remark;

    private String state;

    private List<String> roles;
}
