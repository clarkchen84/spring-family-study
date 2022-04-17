package sizhe.chen.redis;

import io.lettuce.core.ReadFrom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import sizhe.chen.redis.model.Coffee;
import sizhe.chen.redis.model.CoffeeOrder;
import sizhe.chen.redis.service.CoffeeService;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootApplication
@Slf4j
@EnableTransactionManagement
@EnableJpaRepositories
public class RedisApplicationBootStrap  implements ApplicationRunner {
    @Autowired
    private CoffeeService coffeeService;



    @Bean
    public RedisTemplate<String , Coffee> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Coffee> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

       return redisTemplate;
    }

    @Bean
    public LettuceClientConfigurationBuilderCustomizer clientConfigurationBuilderCustomizer(){
        return  builder -> builder.readFrom(ReadFrom.MASTER);
    }

    public static void main(String[] args){
        SpringApplication.run(RedisApplicationBootStrap.class,args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Optional<Coffee> c = coffeeService.findOneCoffee("mocha");

        IntStream.rangeClosed(1,5).forEach(i ->  {
                Optional<Coffee> cf =  coffeeService.findOneCoffee("mocha");
                log.info("Value from Redis: {}", cf);
            }
        );

    }
}
