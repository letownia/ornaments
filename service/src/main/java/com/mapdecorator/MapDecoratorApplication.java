package com.mapdecorator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;

import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;


@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration.class})
// We use direct @Import instead of @ComponentScan to speed up cold starts
//@ComponentScan(basePackages = "com.mapdecorator")
//@Import({ PageController.class, AJAXRequestController.class, MapDecoratorService.class,
//    FileSystemImageStorageService.class, PropertyLogger.class})
//@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration.class})
//@Configuration

public class MapDecoratorApplication extends SpringBootServletInitializer {
  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /*
     * Create required HandlerMapping, to avoid several default HandlerMapping instances being created
     */
    @Bean
    public HandlerMapping handlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    /*
     * Create required HandlerAdapter, to avoid several default HandlerAdapter instances being created
     */
    @Bean
    public HandlerAdapter handlerAdapter() {
        return new RequestMappingHandlerAdapter();
    }

    /*
     * optimization - avoids creating default exception resolvers; not required as the serverless container handles
     * all exceptions
     *
     * By default, an ExceptionHandlerExceptionResolver is created which creates many dependent object, including
     * an expensive ObjectMapper instance.
     *
     * To enable custom @ControllerAdvice classes remove this bean.
     */
//    @Bean
//    public HandlerExceptionResolver handlerExceptionResolver() {
//        return new HandlerExceptionResolver() {
//
//            @Override
//            public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//                return null;
//            }
//        };
//    }

    public static void main(String[] args) {
        try {
            logger.info("String with args " + Arrays.asList(args));
            SpringApplication.run(MapDecoratorApplication.class, args);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
