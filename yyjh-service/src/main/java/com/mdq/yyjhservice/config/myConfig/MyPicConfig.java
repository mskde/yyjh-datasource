//package com.mdq.yyjhservice.config.myConfig;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//
//@Configuration
//public class MyPicConfig extends WebMvcConfigurationSupport {
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
////        registry.addResourceHandler("/imgs/**").addResourceLocations("file:/static/imgs/");
//        //获取文件的真实路径
//        System.out.println(System.getProperty("user.dir"));
////        String path = System.getProperty("user.dir")+"\\yyjj-service\\src\\main\\resources\\static\\imgs\\";
////        String os = System.getProperty("os.name");
////        if (os.toLowerCase().startsWith("win")) {
////            registry.addResourceHandler("/imgs/**").
////                    addResourceLocations("file:"+path);
////        }else{//linux和mac系统
////            registry.addResourceHandler("/imgs/**").
////                    addResourceLocations("file:"+path);
////        }
////        super.addResourceHandlers(registry);
//    }
//}