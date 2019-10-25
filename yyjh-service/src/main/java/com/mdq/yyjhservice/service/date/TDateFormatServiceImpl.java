package com.mdq.yyjhservice.service.date;

import com.mdq.yyjhservice.dao.date.TDateFormatMapper;
import com.mdq.yyjhservice.domain.date.TDateFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Primary
@Slf4j
@Transactional
public class TDateFormatServiceImpl implements TDateFormatService {
    @Autowired
    private TDateFormatMapper tDateFormatMapper;

    @Override
    public TDateFormat getTDateFormatById(Integer id) {
        return tDateFormatMapper.getTDateFormatById(id);
    }

    @Override
    public boolean updTDateFormatById(TDateFormat record) {
        int count=tDateFormatMapper.updTDateFormatById(record);
        return count>0?true:false;
    }

    @Override
    public boolean delTDateFormatById(Integer id) {
        int count=tDateFormatMapper.delTDateFormatById(id);
        return count>0?true:false;
    }

    @Override
    public boolean addTDateFormat(TDateFormat record) {
        int count=tDateFormatMapper.addTDateFormat(record);
        return count>0?true:false;
    }
}
