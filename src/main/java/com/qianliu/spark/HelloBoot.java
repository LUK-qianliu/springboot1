package com.qianliu.spark;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

//@RestController注解表示HelloBoot的生命周期交给RestController来管理
@RestController
public class HelloBoot {

    //@RequestMapping映射该方法
    //value = "/hello"表示访问的链接是：/hello
    //method = RequestMethod.GET表示请求的方法是：get
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String sayHello(){
        return "hello spring boot!";
    }

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public ModelAndView getindex(){
        return new ModelAndView("test"); //test表示的是resources/templates/test.html
    }
}
