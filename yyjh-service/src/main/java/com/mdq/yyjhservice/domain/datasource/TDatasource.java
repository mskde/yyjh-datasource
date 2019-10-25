package com.mdq.yyjhservice.domain.datasource;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class TDatasource {
    private Integer id;

    private String userId;

    private String type;

    @JsonFormat(pattern="yyyy-MM-dd-HH-mm-ss")
    private Date createtime;

    @JsonFormat(pattern="yyyy-MM-dd-HH-mm-ss")
    private Date updatetime;

    private String databaseName;

    private String alias;

    private String driver;

    private String url;

    private Integer port;

    private String auth;

    private String username;

    private String password;

    private String description;

    private String encode;
}