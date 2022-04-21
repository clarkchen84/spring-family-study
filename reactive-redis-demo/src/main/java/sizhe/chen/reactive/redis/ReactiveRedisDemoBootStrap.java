package sizhe.chen.reactive.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@SpringBootApplication
@Slf4j
public class ReactiveRedisDemoBootStrap implements ApplicationRunner {

    private static final String KEY = "Coffee_Menu";

    @Autowired
    private JdbcTemplate jdbcTemplate;



    public static void main(String[] args) {
        SpringApplication.run(ReactiveRedisDemoBootStrap.class,args);
    }

    @Autowired
    private ReactiveStringRedisTemplate redisTemplate;

    @Bean
    private ReactiveStringRedisTemplate reactiveStringRedisTemplate(ReactiveRedisConnectionFactory redisConnectionFactory){
        return new ReactiveStringRedisTemplate(redisConnectionFactory);
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        ReactiveHashOperations<String,String,String> reactiveHashOperations = redisTemplate.opsForHash();
        CountDownLatch count = new CountDownLatch(1);

        List<Coffee> coffeeList = jdbcTemplate.query("Select * from t_coffee" ,
                (rs,i ) ->{
                    return Coffee.builder().id(rs.getLong("id")).name(rs.getString("name")).price(rs.getLong("price"))
                            .createTime(rs.getDate("create_Time"))
                            .updateTime(rs.getDate("update_Time")).build();
                });
        Flux.fromIterable(coffeeList)
                .publishOn(Schedulers.single())
                .doOnComplete(() -> log.info("list ok"))
                .flatMap(c -> {
                    log.info("try to put {}:{}",c.getName(),c.getPrice() );
                    return reactiveHashOperations.put(KEY,c.getName(),c.getPrice().toString());
                })
                .doOnComplete(() -> log.info("set ok"))
                .concatWith(redisTemplate.expire(KEY, Duration.ofMinutes(1)))
                .doOnComplete(() -> log.info("expire ok"))
                .onErrorResume(e -> {
                    log.error("exception {}", e.getMessage());
                    return Mono.just(false);
                })
                .subscribe( b -> log.info("boolean : {}", b)
                ,e -> log.error("Exception {}" ,e.getMessage())
                        ,() -> count.countDown()
                );
        log.info("waiting-----");
        count.await();
    }
}
