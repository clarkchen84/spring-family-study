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
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import sizhe.chen.redis.converter.BytesToMoneyConverter;
import sizhe.chen.redis.converter.MoneyToBytesConverter;
import sizhe.chen.redis.model.Coffee;
import sizhe.chen.redis.service.CoffeeService;

import java.util.Arrays;
import java.util.Optional;

/**
 * @Author: sizhe.chen
 * @Date: Create in 12:35 下午 2022/4/17
 * @Description:
 * @Modified:
 * @Version:
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@EnableRedisRepositories
@Slf4j
public class RedisRepositoryApplicationBootStrap implements ApplicationRunner {
    @Autowired
    private CoffeeService coffeeService;
    @Bean
    public RedisCustomConversions redisCustomConversions(){
        return new RedisCustomConversions(
                Arrays.asList(new MoneyToBytesConverter(),new BytesToMoneyConverter()));
    }
    @Bean
    public LettuceClientConfigurationBuilderCustomizer lettuceClientConfigurationBuilderCustomizer(){
        return clientConfigurationBuilder -> clientConfigurationBuilder.readFrom(ReadFrom.MASTER_PREFERRED);
    }

    public static void main(String[] args) {
        SpringApplication.run(RedisRepositoryApplicationBootStrap.class,args);
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(coffeeService.findAllCoffee());
        Optional<Coffee> c = coffeeService.findSimpleCoffeeFromCache("mocha");
        log.info("Coffee {}", c);

        for (int i = 0; i < 5; i++) {
            c = coffeeService.findSimpleCoffeeFromCache("mocha");
        }

        log.info("Value from Redis: {}", c);
    }
}
