package com.mdq.yyjhservice.service.user;

import com.mdq.yyjhservice.dao.user.TUserMapper;
import com.mdq.yyjhservice.domain.user.TUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Primary
@Transactional
@Slf4j
public class TUserServiceImpl implements TUserService{
    @Autowired
    private TUserMapper tuser;

    @Override
    public boolean createTable() {
        return tuser.createTable();
    }

    @Override
    public boolean insert(TUser record) {
        return tuser.insert(record)>0?true:false;
    }

    @Override
    public int getSum() {
        return tuser.getSum();
    }

    @Override
    public List<TUser> selectAll() {
        return tuser.selectAll();
    }

    @Override
    public List<TUser> selectLimit(String text,int pageSize, int begin) {
        return tuser.selectLimit(text,pageSize,begin);
    }

    @Override
    public String selectLoginIdByUser(String user) {
        return tuser.selectLoginIdByUser(user);
    }

    @Override
    public TUser selectAllByUser(String loginid) {
        return tuser.selectAllByUser(loginid);
    }

    @Override
    public String getUseridByLoginid(String loginId) {
        return tuser.getUseridByLoginid(loginId);
    }

    @Override
    public String getPwdByLoginid(String loginId) {
        return tuser.getPwdByLoginid(loginId);
    }

    @Override
    public String getUserimgByLoginid(String loginId) {
        return tuser.getUserimgByLoginid(loginId);
    }

    @Override
    public String getEmailByLoginid(String loginId) {
        return tuser.getEmailByLoginid(loginId);
    }

    @Override
    public Map<Object, String> getEmail_and_PasswordByLoginid(String loginId) {
        return tuser.getEmail_and_PasswordByLoginid(loginId);
    }

    @Override
    public List<TUser> getUsersByIdsText(List<Integer> ids,String text) {
        return tuser.getUsersByIdsText(ids,text);
    }
    @Override
    public boolean isLoginIdRepeat(String loginId) {
        return tuser.isLoginIdRepeat(loginId)>0?true:false;
    }

    @Override
    public boolean isEmailRepeat(String email) {
        return tuser.isEmailRepeat(email)>0?true:false;
    }

    @Override
    public boolean isTelRepeat(String tel) {
        return tuser.isTelRepeat(tel)>0?true:false;
    }

    @Override
    public boolean updatePasswordByLoginid(String loginId, String password) {
        return tuser.updatePasswordByLoginid(loginId,password)>0?true:false;
    }

    @Override
    public boolean deleteOneByLoginid(String loginId) {
        return tuser.deleteOneByLoginid(loginId) > 0 ? true : false;
    }

    @Override
    public boolean deleteSomeByIds(List<Integer> ids) {
        return tuser.deleteSomeByIds(ids) > 0 ? true : false;
    }

    @Override
    public boolean deleteSomeByLoginid(String[] loginIds) {
        return tuser.deleteSomeByLoginid(loginIds) > 0 ? true : false;
    }

    @Override
    public boolean updateInfoByLoginId(String loginid, String infoname, String value) {
        return tuser.updateInfoByLoginId(loginid,infoname,value) > 0 ? true : false;
    }

    @Override
    public boolean updateStateById(int id, int state) {
        return tuser.updateStateById(id,state) >0 ? true : false;
    }

    @Override
    public boolean deleteOneById(int id) {
        return tuser.deleteOneById(id) > 0 ? true : false;
    }

}
