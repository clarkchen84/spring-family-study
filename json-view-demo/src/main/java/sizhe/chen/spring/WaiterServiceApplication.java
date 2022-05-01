package sizhe.chen.spring;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Slf4j
@EnableCaching
@EnableJpaRepositories
public class WaiterServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaiterServiceApplication.class, args);
    }

    @Bean
    public Hibernate5Module hibernate5Module(){
        return  new Hibernate5Module();
    }

    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(){
        return builder -> builder.indentOutput(true);
    }
}
