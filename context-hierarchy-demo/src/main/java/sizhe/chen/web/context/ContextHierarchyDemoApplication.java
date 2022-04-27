package sizhe.chen.web.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import sizhe.chen.web.foo.FooConfig;

@SpringBootApplication
@Slf4j
public class ContextHierarchyDemoApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(ContextHierarchyDemoApplication.class,args);

    }
    @Override
    public void run(ApplicationArguments args) throws Exception {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(FooConfig.class);
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext(
                new String[]{"classpath:/applicatinoContext.xml"},context);

        TestBean bean = context.getBean("testBeanX",TestBean.class);
        bean.hello();

        log.info("=============");

        bean = classPathXmlApplicationContext.getBean("testBeanX",TestBean.class);

        bean.hello();
        log.info("=============");
        bean = classPathXmlApplicationContext.getBean("testBeanY",TestBean.class);

        bean.hello();



    }
}
