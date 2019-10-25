package com.mdq.yyjhservice.dao.date;

import com.mdq.yyjhservice.domain.date.TDateFormat;
import org.springframework.stereotype.Repository;

@Repository
public interface TDateFormatMapper {


    TDateFormat getTDateFormatById(Integer id);

    int updTDateFormatById(TDateFormat record);

    int delTDateFormatById(Integer id);

    int addTDateFormat(TDateFormat record);

}