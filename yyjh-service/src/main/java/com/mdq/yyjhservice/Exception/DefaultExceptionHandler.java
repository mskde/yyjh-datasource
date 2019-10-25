//package com.mdq.yyjhservice.Exception;
//
//import org.apache.shiro.authz.UnauthorizedException;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@ControllerAdvice
//public class DefaultExceptionHandler {
//    @ExceptionHandler({UnauthorizedException.class})
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public void processUnauthenticatedException(UnauthorizedException e , HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
//        System.out.println("没有权限异常111");
//        request.getRequestDispatcher("/toErrorPage").forward(request,response);
//    }
//}
