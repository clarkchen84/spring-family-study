package sizhe.chen.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories
public class SpringBucksBootStrap {
    public static void main(String[] args) {
        SpringApplication.run(SpringBucksBootStrap.class,args);
    }
}
