package com.mdq.yyjhservice.service.myService;

import com.mdq.yyjhservice.domain.user.TUser;
import com.mdq.yyjhservice.vo.ControllerResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface RegisteService {
    ControllerResult registe(TUser tuser , MultipartFile file) throws IOException;
}
