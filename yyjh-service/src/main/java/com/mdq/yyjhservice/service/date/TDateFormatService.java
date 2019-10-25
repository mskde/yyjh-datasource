package com.mdq.yyjhservice.service.date;

import com.mdq.yyjhservice.domain.date.TDateFormat;

public interface TDateFormatService {
    TDateFormat getTDateFormatById(Integer id);

    boolean updTDateFormatById(TDateFormat record);

    boolean delTDateFormatById(Integer id);

    boolean addTDateFormat(TDateFormat record);
}
