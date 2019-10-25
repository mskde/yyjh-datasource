package com.mdq.yyjhservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mdq.tools.YYJHTools;
import com.mdq.yyjhservice.domain.auth.TPermission;
import com.mdq.yyjhservice.domain.auth.TRole;
import com.mdq.yyjhservice.domain.auth.TRolePermission;
import com.mdq.yyjhservice.domain.datasource.TDatasource;
import com.mdq.yyjhservice.domain.user.TUser;
import com.mdq.yyjhservice.enumeration.DatasourceEnum;
import com.mdq.yyjhservice.pojo.*;
import com.mdq.yyjhservice.service.auth.TPermissionService;
import com.mdq.yyjhservice.service.auth.TRolePermissionService;
import com.mdq.yyjhservice.service.auth.TRoleService;
import com.mdq.yyjhservice.service.auth.TUserRroleService;
import com.mdq.yyjhservice.service.datasource.TDatasourceService;
import com.mdq.yyjhservice.service.date.TDateFormatService;
import com.mdq.yyjhservice.service.myService.AuthorityManageService;
import com.mdq.yyjhservice.service.user.TUserService;
import com.mdq.yyjhservice.utils.DateUtils;
import com.mdq.yyjhservice.utils.MyUtils;
import com.mdq.yyjhservice.vo.ControllerResult;
import org.apache.commons.io.FileUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/datasource")
public class DatasourceController {
    //上传头像保存路径
    @Value("${file.upload.path.login.headImgFile}")
    private String headImg_path;
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private TUserService tu;
    @Autowired
    private TRoleService tr;
    @Autowired
    private TPermissionService tp;
    @Autowired
    private TUserRroleService tur;
    @Autowired
    private TRolePermissionService trp;
    @Autowired
    private AuthorityManageService am;

    @Autowired
    private TDatasourceService tDatasourceService;
    @Autowired
    private TDateFormatService tDateFormatService;

    /**
     * 登出
     */
    @RequestMapping("/logout")
    public void logout(){
        System.out.println("logout");
    }
    /**
     * datasource
     * 登出
     */
    @RequestMapping("/datasource_logout")
    public Object datasource_logout(String user){
        //该方法调用后自动删除shiro登陆信息
        ControllerResult result = new ControllerResult();
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        result.setCode(DatasourceEnum.FAIL.getCode());
        //获取用户loginId(user可能是loginId,email,tel)
        String loginid = tu.selectLoginIdByUser(user);
        System.out.println("loginid:"+loginid);
        if(null != loginid){
            //删除Redis缓存
            Jedis jedis = jedisPool.getResource();
            jedis.del(loginid);
            jedis.close();
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        else{
            result.setPayload("账号登陆异常！系统出错！");
        }
//        response.sendRedirect("/datasource/logout");
        return result;
    }
    /**
     * 获取个人信息
     */
    @RequestMapping("/getOwnInfo")
    public Object getOwnInfo(String user){
        ControllerResult result = new ControllerResult();
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        result.setCode(DatasourceEnum.FAIL.getCode());
        //获取个人信息
        TUser tuser = tu.selectAllByUser(user);
        if(null != tuser){
            InfoData infodatas = new InfoData();
            //获取个人角色名
            List<TRole> roles = tr.selectRolenamesByLoginid(tuser.getLoginid());
            List<String> myroles = new ArrayList<String>();
            if(roles != null){
                for(TRole role : roles)
                    myroles.add(role.getRolename());
                infodatas.setEmail(tuser.getEmail());
                infodatas.setId(tuser.getId());
                infodatas.setLoginid(tuser.getLoginid());
                infodatas.setNickname(tuser.getNickname());
                infodatas.setRemark(tuser.getRemark());
                infodatas.setState(tuser.getState());
                infodatas.setUserimg(tuser.getUserimg());
                infodatas.setTel(tuser.getTel());
                infodatas.setRoles(myroles);
                result.setPayload(infodatas);
            }
            else{
                result.setPayload(tuser);
            }
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        return result;
    }
    /**
     * 权限管理
     * 用户信息表
     */
    @RequestMapping("/getAuthorityUserDatas")
    @RequiresPermissions(value={"Select"} , logical = Logical.AND)
    public Object getAuthorityUserDatas(@RequestParam("pageSize") int pageSize
                                        ,@RequestParam("pageNumber")int pageNumber
                                        ,String text){
        //获取t_user用户数据
        int begin = (pageNumber - 1) * pageSize;
        List<TUser> tusers = tu.selectLimit(text,pageSize,begin);
        ObjectNode datas  = new ObjectMapper().createObjectNode();
        if(null != tusers){
            ArrayNode  rows  = new ObjectMapper().createArrayNode();
            //获取带角色名的整张t_user_role表
            List<MyTUserRole> userRoles = tur.selectMyAll();
            for(TUser tuser : tusers){
                ObjectNode row = new ObjectMapper().createObjectNode();
                row.put("id",tuser.getId());
                row.put("userimg",tuser.getUserimg());
                row.put("loginid",tuser.getLoginid());
                row.put("nickname",tuser.getNickname());
                row.put("state",tuser.getState());
                List<String> roles = new ArrayList<String>();
                for(int i=0;i<userRoles.size();i++){
                    if(tuser.getId().toString().equals(userRoles.get(i).getUserId().toString()))
                        roles.add(userRoles.get(i).getRoleName());
                }
                String rolesToString = roles.toString();
                rolesToString = rolesToString.substring(1,rolesToString.length()-1);
                rolesToString = rolesToString.replaceAll(" ","");
                row.put("roles",rolesToString);
                rows.add(row);
            }
            Integer sum = tu.getSum();
            sum = sum != null ? sum : 0;
            datas.put("total",sum);
            datas.put("rows",rows);
        }
        return datas;
    }
    /**
     * 权限管理
     * 角色信息表
     */
    @RequestMapping("/getAuthorityRoleDatas")
    @RequiresPermissions(value={"Select"} , logical = Logical.AND)
    public Object getAuthorityRoleDatas(@RequestParam("pageSize") int pageSize
                                    ,@RequestParam("pageNumber")int pageNumber
                                    ,String text){
        //获取t_role数据
        int begin = (pageNumber - 1) * pageSize;
        List<TRole> troles = tr.selectLimit(text,pageSize,begin);
        ObjectNode datas  = new ObjectMapper().createObjectNode();
        if(null != troles){
            ArrayNode  rows  = new ObjectMapper().createArrayNode();
            for(TRole trole : troles){
                ObjectNode row = new ObjectMapper().createObjectNode();
                row.put("id",trole.getId());
                row.put("rolename",trole.getRolename());
                row.put("roledesc",trole.getRoledesc());
                row.put("state",trole.getState());
                rows.add(row);
            }
            Integer sum = tr.getSum();
            sum = sum != null ? sum : 0;
            datas.put("total",sum);
            datas.put("rows",rows);
        }
        return datas;
    }
    /**
     * 权限管理
     * 权限信息表
     */
    @RequestMapping("/getAuthorityPermDatas")
    @RequiresPermissions(value={"Select"} , logical = Logical.AND)
    public Object getAuthorityPermDatas(@RequestParam("pageSize") int pageSize
                                        ,@RequestParam("pageNumber")int pageNumber
                                        ,String text){
        //获取t_permission数据
        int begin = (pageNumber - 1) * pageSize;
        List<TPermission> tpermissions = tp.selectLimit(text,pageSize,begin);
        ObjectNode datas  = new ObjectMapper().createObjectNode();
        if(null != tpermissions){
            ArrayNode  rows  = new ObjectMapper().createArrayNode();
            //搜索整张带rolename的t_role_permission表
            List<MyTRolePermission> rps = trp.selectMyAll();
            for(TPermission tpermission : tpermissions){
                ObjectNode row = new ObjectMapper().createObjectNode();
                row.put("id",tpermission.getId());
                row.put("permission",tpermission.getPermission());
                row.put("state",tpermission.getState());
                List<String> roles = new ArrayList<String>();
                for(int i=0;i<rps.size();i++){
                    if(tpermission.getId().toString().equals(rps.get(i).getPermissionId().toString()))
                        roles.add(rps.get(i).getRoleName());
                }
                String rolesToString = roles.toString();
                rolesToString = rolesToString.substring(1,rolesToString.length()-1);
                rolesToString = rolesToString.replaceAll(" ","");
                row.put("roles",rolesToString);
                rows.add(row);
            }
            Integer sum = tp.getSum();
            sum = sum != null ? sum : 0;
            datas.put("total",sum);
            datas.put("rows",rows);
        }
        return datas;
    }
    /**
     * 权限管理
     * 角色信息表
     * 角色用户表
     * 根据role_id获取用户数据
     */
    @RequestMapping("/getRoleUserDatas")
    @RequiresPermissions(value={"Select"} , logical = Logical.AND)
    public Object getRoleUserDatas(int role_id
                                    ,@RequestParam("pageSize") int pageSize
                                    ,@RequestParam("pageNumber")int pageNumber
                                    ,String text){
        ObjectNode datas  = new ObjectMapper().createObjectNode();
        //获取用户ids
        int begin = (pageNumber - 1) * pageSize;
        List<Integer> ids = tur.selectUserIdByLimit(role_id,pageSize,begin);
        if(null != ids){
            //获取用户数据
            List<TUser> userDatas = new ArrayList<TUser>();
            if(ids.size() > 0)
                userDatas = tu.getUsersByIdsText(ids,text);
            if(null != userDatas){
                //获取用户角色
                List<MyTUserRole> userRoles = new ArrayList<MyTUserRole>();
                if(ids.size() > 0)
                    userRoles = tur.selectMyAllByUserIds(ids);
                //将数据存入payload(调数据格式)
                ArrayNode  rows  = new ObjectMapper().createArrayNode();
                for(TUser tuser : userDatas){
                    ObjectNode row = new ObjectMapper().createObjectNode();
                    row.put("id",tuser.getId());
                    row.put("userimg",tuser.getUserimg());
                    row.put("loginid",tuser.getLoginid());
                    row.put("nickname",tuser.getNickname());
                    row.put("state",tuser.getState());
                    List<String> roles = new ArrayList<String>();
                    for(int i=0;i<userRoles.size();i++){
                        if(tuser.getId().toString().equals(userRoles.get(i).getUserId().toString()))
                            roles.add(userRoles.get(i).getRoleName());
                    }
                    String rolesToString = roles.toString();
                    rolesToString = rolesToString.substring(1,rolesToString.length()-1);
                    rolesToString = rolesToString.replaceAll(" ","");
                    row.put("roles",rolesToString);
                    rows.add(row);
                }
                Integer sum = tur.getSumByRoleId(role_id);
                sum = sum != null ? sum : 0;
                datas.put("total",sum);
                datas.put("rows",rows);
            }
        }
        return datas;
    }
    /**
     * 权限管理
     * 角色信息表
     * 角色权限表
     * 根据role_id获取权限数据
     */
    @RequestMapping("/getRolePermDatas")
    @RequiresPermissions(value={"Select"} , logical = Logical.AND)
    public Object getRolePermDatas(int role_id
                                ,@RequestParam("pageSize") int pageSize
                                ,@RequestParam("pageNumber")int pageNumber
                                ,String text){
        ObjectNode datas  = new ObjectMapper().createObjectNode();
        //获取权限ids
        int begin = (pageNumber - 1) * pageSize;
        List<Integer> ids = trp.selectPermIdsByLimit(role_id,pageSize,begin);
        if(null != ids){
            //获取用户数据
            List<TPermission> permDatas = new ArrayList<TPermission>();
            if(ids.size() > 0)
                permDatas = tp.selectAllByIdsText(ids,text);
            if(null != permDatas){
                //获取权限角色
                List<MyTRolePermission> roles = new ArrayList<MyTRolePermission>();
                if(ids.size() > 0)
                    roles = trp.selectRolesByPermIds(ids);
                //将数据存入payload(调数据格式)
                ArrayNode  rows  = new ObjectMapper().createArrayNode();
                for(TPermission tperm : permDatas){
                    ObjectNode row = new ObjectMapper().createObjectNode();
                    row.put("id",tperm.getId());
                    row.put("permission",tperm.getPermission());
                    row.put("state",tperm.getState());
                    List<String> myroles = new ArrayList<String>();
                    for(int i=0;i<roles.size();i++){
                        if(roles.get(i).getPermissionId().toString().equals(tperm.getId().toString()))
                            myroles.add(roles.get(i).getRoleName());
                    }
                    String rolesToString = myroles.toString();
                    rolesToString = rolesToString.substring(1,rolesToString.length()-1);
                    rolesToString = rolesToString.replaceAll(" ","");
                    row.put("roles",rolesToString);
                    rows.add(row);
                }
                Integer sum = trp.getSumByRoleId(role_id);
                sum = sum != null ? sum : 0;
                datas.put("total",sum);
                datas.put("rows",rows);
            }
        }
        return datas;
    }
    /**
     * 用户信息表
     * 删除一条信息
     */
    @RequestMapping("/deleteAuthorityUser")
    @RequiresPermissions(value={"Delete"} , logical=Logical.AND)
    @RequiresRoles(value={"超级管理员","管理员"},logical=Logical.OR)
    public Object deleteAuthorityUser(int id){
        ControllerResult result = new ControllerResult();
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        result.setCode(DatasourceEnum.FAIL.getCode());
        //判断用户-角色是否存在映射
        List<Integer> TURs = tur.selectRolesByUserId(id);
        boolean flag1 = true;
        if(null != TURs && TURs.size()>0){
            //存在
            //先删除对应用户-角色的映射
            flag1 = tur.deleteAllByUserId(id);
        }
        if(flag1){
            //删除一条用户信息
            boolean flag2 = tu.deleteOneById(id);
            if(flag2){
                result.setCode(DatasourceEnum.SUCCESS.getCode());
                result.setMsg(DatasourceEnum.SUCCESS.getMsg());
            }
            else{
                result.setPayload("删除失败！");
            }
        }
        else{
            result.setPayload("删除失败！");
        }
        return result;
    }
    /**
     * 角色信息表
     * 删除一条信息
     */
    @RequestMapping("/deleteAuthorityRole")
    @RequiresPermissions(value={"Delete","ManageRole"} , logical=Logical.AND)
    public Object deleteAuthorityRole(int id){
        ControllerResult result = new ControllerResult();
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        result.setCode(DatasourceEnum.FAIL.getCode());
        boolean flag = tr.deleteOneById(id);
        if(flag){
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        else{
            result.setPayload("删除失败！");
        }
        return result;
    }
    /**
     * 权限信息表
     * 删除一条信息
     */
    @RequestMapping("/deleteAuthorityPerm")
    @RequiresPermissions(value={"Delete","ManagePermission"} , logical=Logical.AND)
    public Object deleteAuthorityPerm(int permission_id){
        ControllerResult result = new ControllerResult();
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        result.setCode(DatasourceEnum.FAIL.getCode());
        boolean flag = tp.deleteOneById(permission_id);
        if(flag){
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        else{
            result.setPayload("删除失败！");
        }
        return result;
    }
    /**
     * 角色信息表中
     * 角色用户表
     * 删除一条信息
     */
    @RequestMapping("/deleteAuthorityRoleUser")
    @RequiresPermissions(value={"Delete","ManageRole"} , logical=Logical.AND)
    public Object deleteAuthorityRoleUser(int role_id , int user_id){
        ControllerResult result = new ControllerResult();
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        result.setCode(DatasourceEnum.FAIL.getCode());
        boolean flag = tur.deleteOneByRoleUser(role_id,user_id);
        if(flag){
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        return result;
    }
    /**
     * 角色信息表中
     * 角色权限表
     * 删除一条信息
     */
    @RequestMapping("/deleteAuthorityRolePerm")
    @RequiresPermissions(value={"Delete","ManagePermission"} , logical=Logical.AND)
    public Object deleteAuthorityRolePerm(int role_id , int permission_id){
        ControllerResult result = new ControllerResult();
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        result.setCode(DatasourceEnum.FAIL.getCode());
        boolean flag = trp.deleteOneByRolePermission(role_id,permission_id);
        if(flag){
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        return result;
    }
    /**
     * 删除多条数据
     */
    //用户信息表
    @RequestMapping("/deleteTUserSome")
    @RequiresPermissions(value={"Delete"} , logical=Logical.AND)
    @RequiresRoles(value={"超级管理员","管理员"},logical=Logical.OR)
    public Object deleteTUserSome(int[] ids ,@RequestParam(required = false) int role_id){
        ControllerResult result = new ControllerResult();
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        result.setCode(DatasourceEnum.FAIL.getCode());
        if(null !=ids){
            if(ids.length > 0){
                //将int[]-->List<Integer>
                List<Integer> lids = new ArrayList<Integer>();
                for(int i=0;i<ids.length;i++)
                    lids.add(ids[i]);
                boolean flag = tu.deleteSomeByIds(lids);
                if(flag){
                    result.setCode(DatasourceEnum.SUCCESS.getCode());
                    result.setMsg(DatasourceEnum.SUCCESS.getMsg());
                }
            }
            else{
                result.setPayload("请选择删除的数据！");
            }
        }
        return result;
    }
    //角色用户表
    @RequestMapping("/deleteTUserRoleSome")
    @RequiresPermissions(value={"Delete","ManageRole"} , logical=Logical.AND)
    public Object deleteTUserRoleSome(int[] ids ,@RequestParam(required = false) int role_id){
        ControllerResult result = new ControllerResult();
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        result.setCode(DatasourceEnum.FAIL.getCode());
        if(null !=ids){
            if(ids.length > 0){
                //将int[]-->List<Integer>
                List<Integer> lids = new ArrayList<Integer>();
                for(int i=0;i<ids.length;i++)
                    lids.add(ids[i]);
                boolean flag = tur.deleteSomeByURId(lids,role_id);
                if(flag){
                    result.setCode(DatasourceEnum.SUCCESS.getCode());
                    result.setMsg(DatasourceEnum.SUCCESS.getMsg());
                }
            }
            else{
                result.setPayload("请选择删除的数据！");
            }
        }
        return result;
    }
    //角色权限表
    @RequestMapping("/deleteTRolePermSome")
    @RequiresPermissions(value={"Delete","ManagePermission"} , logical=Logical.AND)
    public Object deleteTRolePermSome(int[] ids ,@RequestParam(required = false) int role_id){
        ControllerResult result = new ControllerResult();
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        result.setCode(DatasourceEnum.FAIL.getCode());
        if(null !=ids){
            if(ids.length > 0){
                //将int[]-->List<Integer>
                List<Integer> lids = new ArrayList<Integer>();
                for(int i=0;i<ids.length;i++)
                    lids.add(ids[i]);
                boolean flag = trp.deleteSomeByPRId(lids,role_id);
                if(flag){
                    result.setCode(DatasourceEnum.SUCCESS.getCode());
                    result.setMsg(DatasourceEnum.SUCCESS.getMsg());
                }
            }
            else{
                result.setPayload("请选择删除的数据！");
            }
        }
        return result;
    }
    /**
     * 角色权限表
     * 添加一条数据
     */
    @RequestMapping("/authorityAddRolePermission")
    @RequiresPermissions(value={"Insert","ManagePermission"} , logical=Logical.AND)
    public Object authorityAddRolePermission(int role_id,String permission){
        ControllerResult result = new ControllerResult();
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        result.setCode(DatasourceEnum.FAIL.getCode());
        //查询映射是否存在
        TRolePermission rp = trp.selectOneByRP(role_id,permission);
        if(null != rp){
            result.setPayload("映射已存在！");
        }
        else{
            //向t_role_permission中添加一条数据
            boolean flag1 = trp.insertOneByRP(role_id,permission);
            if(flag1){
                result.setCode(DatasourceEnum.SUCCESS.getCode());
                result.setMsg(DatasourceEnum.SUCCESS.getMsg());
            }
        }
        return result;
    }
    /**
     * 用户信息表
     * 修改用户角色
     */
    @RequestMapping("/changeAuthorityUserRoles")
    @RequiresPermissions(value={"Update","ManageRole"} , logical = Logical.AND)
    public Object changeAuthorityUserRoles(ChangeAuthorityUserRolesData data){
        ControllerResult result = new ControllerResult();
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        result.setCode(DatasourceEnum.FAIL.getCode());
        if(null == data.getRoles()){
            data.setRoles(new ArrayList<Integer>());
        }
        result = am.updateUserRoles(data.getId(),data.getRoles(),tur);
        return result;
    }
    /**
     * 权限信息表
     * 修改角色权限
     */
    @RequestMapping("/changeAuthorityRolePermissions")
    @RequiresPermissions(value={"Update","ManagePermission"} , logical = Logical.AND)
    public Object changeAuthorityRolePermissions(ChangeAuthorityUserRolesData data){
        ControllerResult result = new ControllerResult();
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        result.setCode(DatasourceEnum.FAIL.getCode());
        if(null == data.getRoles()){
            data.setRoles(new ArrayList<Integer>());
        }
        result = am.updatePermissionRoles(data.getId(),data.getRoles(),trp);
        return result;
    }
    /**
     * 用户信息表
     * 修改state状态
     */
    @RequestMapping("/changeTUserState")
    @RequiresPermissions(value={"Update"} , logical=Logical.AND)
    @RequiresRoles(value={"超级管理员","管理员"} , logical= Logical.OR)
    public Object changeTUserState(int id,int state){
        ControllerResult result = new ControllerResult();
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        result.setCode(DatasourceEnum.FAIL.getCode());
        boolean flag = tu.updateStateById(id,state);
        if(flag){
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        return result;
    }
    /**
     * 角色信息表
     * 修改state状态
     */
    @RequestMapping("/changeTRoleState")
    @RequiresPermissions(value={"Update","ManageRole"} , logical=Logical.AND)
    public Object changeTRoleState(int id,int state){
        ControllerResult result = new ControllerResult();
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        result.setCode(DatasourceEnum.FAIL.getCode());
        boolean flag = tr.updateStateById(id,state);
        if(flag){
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        return result;
    }
    /**
     * 权限信息表
     * 修改state状态
     */
    @RequestMapping("/changeTPermState")
    @RequiresPermissions(value={"Update","ManagePermission"} , logical=Logical.AND)
    public Object changeTPermState(int id,int state){
        ControllerResult result = new ControllerResult();
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        result.setCode(DatasourceEnum.FAIL.getCode());
        boolean flag = tp.updateStateById(id,state);
        if(flag){
            result.setCode(DatasourceEnum.SUCCESS.getCode());
            result.setMsg(DatasourceEnum.SUCCESS.getMsg());
        }
        return result;
    }
    /**
     * 个人信息
     * 修改用户头像
     */
    @RequestMapping("/infoChangeUserimg")
    public Object infoChangeUserimg(@RequestParam(value="info_userimg",required = false) MultipartFile file
                                    ,@RequestParam("loginid") String loginid) throws IOException {
        System.out.println(loginid);
        ControllerResult result = new ControllerResult();
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        result.setCode(DatasourceEnum.FAIL.getCode());
        if(null != loginid){
            File dir = new File(headImg_path);
            String path = ""; //用户头像上传后的路径
            if(!dir.exists())
                dir.mkdirs();
            String headname = file.getOriginalFilename();
            String headsuffix = headname.substring(headname.lastIndexOf('.'));
            String name = YYJHTools.get32UUID() + headsuffix;
            path = headImg_path + File.separator + name;
            String oldUserimg = tu.getUserimgByLoginid(loginid);
            //修改库数据
            boolean flag = tu.updateInfoByLoginId(loginid,"userimg",name);
            if(flag){
                //修改成功
                //上传头像
                File myUserimg = new File(path);
                FileUtils.copyInputStreamToFile(file.getInputStream(),myUserimg);
                //删除原始头像
                System.out.println(oldUserimg);
                if(oldUserimg != null)
                    FileUtils.forceDelete(new File(headImg_path + File.separator + oldUserimg));
                result.setCode(DatasourceEnum.SUCCESS.getCode());
                result.setMsg(DatasourceEnum.SUCCESS.getMsg());
            }
            else{
                result.setPayload("数据入库失败！");
            }
        }
        return result;
    }
    /**
     * 个人信息
     * 修改单条
     * value     ： 修改的值
     * infoname  ： 修改的属性名
     */
    @RequestMapping("/infoChangeSimpleInfo")
    @RequiresPermissions(value={"Update"} , logical=Logical.AND)
    public Object infoChangeNickname(String loginid,String infoname,String value){
        ControllerResult result = new ControllerResult();
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        result.setCode(DatasourceEnum.FAIL.getCode());
        //如果infoname = loginid/email/tel
        //查重
        System.out.println(infoname);
        if("loginid".equals(infoname) || "email".equals(infoname) || "tel".equals(infoname)){
            Map<Object,String> datas = new HashMap<Object,String>();
            datas.put("loginid" , value);
            datas.put("email",value);
            datas.put("tel",value);
            result = MyUtils.isMyAccountRepeat(tu,datas);
        }
        if(result.getPayload() != null){
            //重写payload
            result.setPayload("已被注册过，请重新输入！");
        }
        else{
            //信息可用
            //修改单条个人信息
            boolean flag = tu.updateInfoByLoginId(loginid,infoname,value);
            if(flag){
                result.setCode(DatasourceEnum.SUCCESS.getCode());
                result.setMsg(DatasourceEnum.SUCCESS.getMsg());
            }
        }
        return result;
    }







    @RequestMapping("/getAll")
    @RequiresPermissions(value={"Select"} , logical=Logical.AND)
    public List<TDatasource> getAll() {
        List<TDatasource> ltd=tDatasourceService.getAll();
        for(TDatasource td: ltd){
            td.setPassword("**");
            td.setEncode("**");
        }
        return ltd;
    }

    @RequestMapping("/findTDatasourceById/{id}")
    @RequiresPermissions(value={"Select"} , logical=Logical.AND)
    public TDatasource findTDatasourceById(@PathVariable Integer id) {
        TDatasource td=tDatasourceService.findTDatasourceById(id);
        td.setPassword("**");
        return td;
    }

    @RequestMapping("/getAllByPage")
    @RequiresPermissions(value={"Select"} , logical=Logical.AND)
    public Object getAllByPage(int pageSize,int pageNumber) throws IOException {
        // 查看全部数据执行后端分页查询
        Map<String,Integer> queryMap = new HashMap<String,Integer>();
        if (pageNumber <= 1){
            pageNumber = 1;
        }
        int beginNumber = (pageNumber - 1)* pageSize;
        queryMap.put("beginNumber", beginNumber);
        queryMap.put("limit", pageSize);

        List<TDatasource> ltd=tDatasourceService.getAllByPage(queryMap);

        ObjectMapper om=new ObjectMapper();
        List<Map<String,String>> listmap=new ArrayList<>();
        //获取日期格式数据值
        int index=tDateFormatService.getTDateFormatById(1).getDateFlag();
        for(TDatasource td:ltd){
            td.setPassword("**");
            td.setEncode("**");

            String json=om.writeValueAsString(td);
            HashMap<String,String> mmap = om.readValue(json, HashMap.class);
            if(mmap.get("createtime") != null){
                mmap.put("createtime", DateUtils.setDateFormat(mmap.get("createtime"),index));
            }
            if(mmap.get("updatetime") != null){
                mmap.put("updatetime", DateUtils.setDateFormat(mmap.get("updatetime"),index));
            }
            listmap.add(mmap);
        }

        int total = tDatasourceService.getCount();
        Map<String,Object> responseMap = new HashMap<String,Object>();
        responseMap.put("rows", listmap);

        // 需要返回到前台，用于计算分页导航栏
        responseMap.put("total", total);
        return responseMap;
    }

    @RequestMapping("/getTDatasourceByAll")
    @RequiresPermissions(value={"Select"} , logical=Logical.AND)
    public Object getTDatasourceByAll(int pageSize,int pageNumber,String info,String selectType) throws IOException {
        System.out.println("===============================");
        System.out.println(info);
        System.out.println(selectType);

        if (pageNumber <= 1){
            pageNumber = 1;
        }
        int beginNumber = (pageNumber - 1)* pageSize;
        int limit=pageSize;
        int total;
        List<TDatasource> ltd=new ArrayList<TDatasource>();
        if(selectType.equals("any")){
            ltd=tDatasourceService.getTDatasourceByOtherFields(info,beginNumber,limit);
            total=tDatasourceService.getCountByOtherFields(info);
        }else if(info.equals("")){
            ltd=tDatasourceService.getTDatasourceByType(selectType,beginNumber,limit);
            total=tDatasourceService.getCountByType(selectType);
        }else {
            ltd=tDatasourceService.getTDatasourceByAll(info,selectType,beginNumber,limit);
            total=tDatasourceService.getCountByAll(info,selectType);
        }
        ObjectMapper om=new ObjectMapper();
        List<Map<String,String>> listmap=new ArrayList<>();
        //获取日期格式数据值
        int index=tDateFormatService.getTDateFormatById(1).getDateFlag();
        for(TDatasource td:ltd){
            td.setPassword("**");
            td.setEncode("**");

            String json=om.writeValueAsString(td);
            HashMap<String,String> mmap = om.readValue(json, HashMap.class);
            if(mmap.get("createtime") != null){
                mmap.put("createtime", DateUtils.setDateFormat(mmap.get("createtime"),index));
            }
            if(mmap.get("updatetime") != null){
                mmap.put("updatetime", DateUtils.setDateFormat(mmap.get("updatetime"),index));
            }
            listmap.add(mmap);
        }

        Map<String,Object> responseMap = new HashMap<String,Object>();
        responseMap.put("rows", listmap);

        // 需要返回到前台，用于计算分页导航栏
        responseMap.put("total", total);
        return responseMap;
    }

    //批量删除数据源表信息(其他未改动)
    @RequestMapping("/delBatchTDatasource")
    @RequiresPermissions(value={"Delete"} , logical=Logical.AND)
    public boolean delBatchTDatasource(@RequestBody String json) throws IOException {
        ObjectMapper om=new ObjectMapper();
        List<Map<String,Integer>> listmap=new ArrayList<Map<String,Integer>>();
        listmap=om.readValue(json,listmap.getClass());
        int[] arr=new int[listmap.size()];
        for(int i=0;i<listmap.size();i++){
            Map<String,Integer> map=listmap.get(i);
            arr[i]=map.get("id");
        }
        return tDatasourceService.delBatchTDatasource(arr);
    }

    //删除数据源表信息(其他未改动)
    @RequestMapping("/delTDatasourceById/{id}")
    @RequiresPermissions(value={"Delete"} , logical=Logical.AND)
    public boolean delTDatasourceById(@PathVariable Integer id){
        return tDatasourceService.delTDatasourceById(id);
    }

    //只修改数据源表格信息(其他未改动)   修改alias会联动地改变encode的filename
    @RequestMapping("/updTDatasourceById")
    @RequiresPermissions(value={"Update"} , logical=Logical.AND)
    public boolean updTDatasourceById(@RequestBody String json) throws IOException {
        ObjectMapper om=new ObjectMapper();
        TDatasource record=om.readValue(json,TDatasource.class);
        record.setUpdatetime(new Date());
        if(record.getType()!= null){
            if(record.getType().equals("excel")){
                TDatasource td=tDatasourceService.findTDatasourceById((Integer)record.getId());
                Map<String,Object> encode_map=new HashMap<>();
                encode_map=om.readValue(td.getEncode(),encode_map.getClass());
                encode_map.put("filename",record.getAlias());
                String encode_json=om.writeValueAsString(encode_map);
                record.setEncode(encode_json);
            }
        }
        return tDatasourceService.updTDatasourceById(record);
    }

}
