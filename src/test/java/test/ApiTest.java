package test;


import cn.zyj.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;
import test.bean.IUserService;

public class ApiTest {
    @Test
    public void test_autoProxy() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:aop.xml");
        IUserService userService = applicationContext.getBean("userService", IUserService.class);
        System.out.println("测试结果：" + userService.queryUserInfo());
    }


}
