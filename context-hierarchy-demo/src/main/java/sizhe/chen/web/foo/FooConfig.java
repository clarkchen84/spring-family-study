package sizhe.chen.web.foo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import sizhe.chen.web.context.TestBean;

@Configuration
@EnableAspectJAutoProxy
public class FooConfig {

    @Bean
    public FooAspect fooAspect(){
        return new FooAspect();
    }

    @Bean
    public TestBean testBeanY(){
        return new TestBean("foo");
    }
    @Bean
    public TestBean testBeanX(){
        return new TestBean("foo");
    }
}
