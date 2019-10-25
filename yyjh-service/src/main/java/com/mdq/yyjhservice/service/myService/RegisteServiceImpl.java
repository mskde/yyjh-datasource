package com.mdq.yyjhservice.service.myService;

import com.mdq.tools.YYJHTools;
import com.mdq.yyjhservice.domain.auth.TRolePermission;
import com.mdq.yyjhservice.domain.auth.TUserRrole;
import com.mdq.yyjhservice.domain.user.TUser;
import com.mdq.yyjhservice.enumeration.DatasourceEnum;
import com.mdq.yyjhservice.service.auth.TUserRroleService;
import com.mdq.yyjhservice.service.user.TUserService;
import com.mdq.yyjhservice.utils.MyUtils;
import com.mdq.yyjhservice.vo.ControllerResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Primary
@Transactional
@Slf4j
public class RegisteServiceImpl implements RegisteService {
    //默认头像名
    public static final String init_headimg = "init_headimg.jpg";
    //上传头像保存路径
    @Value("${file.upload.path.login.headImgFile}")
    private String headImg_path;
    @Autowired
    private TUserService tu;
    @Autowired
    private TUserRroleService tur;

    public ControllerResult registe(TUser tuser , MultipartFile file) throws IOException {
        ControllerResult result=new ControllerResult();
        result.setCode(DatasourceEnum.FAIL.getCode());
        result.setMsg(DatasourceEnum.FAIL.getMsg());
        Map<Object,String> datas = new HashMap<Object,String>();
        datas.put("loginid",tuser.getLoginid());
        datas.put("email",tuser.getEmail());
        datas.put("tel",tuser.getTel());
        //账号查重
        result = MyUtils.isMyAccountRepeat(tu,datas);
        System.out.println(result.toString());
        System.out.println(result.getPayload()+" "+(result.getPayload() == null));
        if(result.getPayload() == null){
            //账号可用
            //用户头像上传
            String path = ""; //用户头像上传后的路径
            if(null != file){
                //创建目录
                File dir = new File(headImg_path);
                if(!dir.exists())
                    dir.mkdirs();
                //头像名
                String headname = file.getOriginalFilename();
                String headsuffix = headname.substring(headname.lastIndexOf('.'));
                String name = YYJHTools.get32UUID() + headsuffix;
                path = headImg_path + File.separator + name;
                //设置用户头像信息
                tuser.setUserimg(name);
            }
            else{
                //用户没有上传头像，设置默认头像
                tuser.setUserimg(init_headimg);
            }
            //用户信息存库
            boolean flag  = tu.insert(tuser);
            if(flag){
                //存图
                if(null != file){
                    File myfile = new File(path);
                    FileUtils.copyInputStreamToFile(file.getInputStream() , myfile);
                }
                //获取用户id
                String user_id = tu.getUseridByLoginid(tuser.getLoginid());
                if(null != user_id && Integer.parseInt(user_id) > 0){
                    //用户-角色映射存库
                    TUserRrole tuserrole = new TUserRrole();
                    tuserrole.setRoleId(3);
                    tuserrole.setUserId(Integer.parseInt(user_id));
                    boolean flag2 = tur.insert(tuserrole);
                    if(flag2){
                        result.setCode(DatasourceEnum.SUCCESS.getCode());
                        result.setMsg(DatasourceEnum.SUCCESS.getMsg());
                    }
                    else{
                        result.setPayload("网络出错，注册失败！");
                    }
                }
            }
            else{
                result.setPayload("网络出错，注册失败！");
            }
        }
        return result;
    }
}
