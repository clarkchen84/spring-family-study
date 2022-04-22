package sizhe.chen.reactive.mongo;

import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import sizhe.chen.reactive.mongo.converter.MoneyReadConverter;
import sizhe.chen.reactive.mongo.converter.MoneyWriteConverter;
import sizhe.chen.reactive.mongo.model.Coffee;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @Author: sizhe.chen
 * @Date: Create in 8:17 下午 2022/4/22
 * @Description:
 * @Modified:
 * @Version:
 */
@SpringBootApplication
@Slf4j
public class ReactiveMongoDemoBootStrap implements ApplicationRunner {



    public static void main(String[] args) {
        SpringApplication.run(ReactiveMongoDemoBootStrap.class, args);
    }
    @Autowired
    private ReactiveMongoTemplate template;
    private CountDownLatch countDownLatch = new CountDownLatch(2);

    @Bean
    public MongoCustomConversions mongoCustomConversions(){
        return new MongoCustomConversions(List.of(new MoneyReadConverter(),new MoneyWriteConverter()));
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        startFromInsertion(() -> {
            decreaseHighPrice();
        });

        log.info("after starting ");

        countDownLatch.await();
    }

    private void startFromInsertion(Runnable runnable){
        template.insertAll(initCoffee())
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(c -> log.info("Next: {} ",c))
                .doOnComplete(runnable)
                .doFinally(s -> {
                    countDownLatch.countDown();
                    log.info("Finally 1, {} ", s);
                })
                .count()
                .subscribe(c -> log.info("insert {} records", c));
    }

    private void decreaseHighPrice(){
        template.updateMulti(
                Query.query(where("price").gte(3000L)),
                new Update().inc("price", -500L)
                .currentDate("updateTime"),Coffee.class)
                .doFinally(
                        s -> {
                            countDownLatch.countDown();
                            log.info("Finally 2 {}" , s);
                        }
                )
                .subscribe(r -> log.info("Result is {}", r));


    }

    private List<Coffee> initCoffee(){
        Coffee espresso = Coffee.builder()
                .name("espresso")
                .price(Money.of(CurrencyUnit.of("CNY"),20.0))
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        Coffee latte  = Coffee.builder()
                .name("latte")
                .price(Money.of(CurrencyUnit.of("CNY"),35.0))
                .createTime(new Date())
                .updateTime(new Date())
                .build();

        return Arrays.asList(espresso,latte);

    }

}
