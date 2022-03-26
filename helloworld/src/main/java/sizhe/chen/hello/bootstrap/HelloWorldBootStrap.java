package sizhe.chen.hello.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: sizhe.chen
 * @Date: Create in 9:55 下午 2022/3/26
 * @Description:
 * @Modified:
 * @Version:
 */
@SpringBootApplication
@RestController
public class HelloWorldBootStrap {
    public static void main(String[] args) {
        SpringApplication.run(HelloWorldBootStrap.class,args);
    }
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String hello(){
        return "hello world";
    }
}
