package sizhe.chen.mongo;

import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.ui.context.Theme;
import sizhe.chen.mongo.converter.MoneyReadConverter;
import sizhe.chen.mongo.model.Coffee;
import sizhe.chen.mongo.repository.CoffeeRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@SpringBootApplication
@EnableMongoRepositories
@Slf4j
public class MogoRepositoryBootStrap implements ApplicationRunner {
    @Autowired
    private CoffeeRepository coffeeRepository;

    public static void main(String[] args) {
        SpringApplication.run(MogoRepositoryBootStrap.class,args);
    }

    @Bean
    public MongoCustomConversions mongoCustomConversions(){
        return  new MongoCustomConversions(Arrays.asList(new MoneyReadConverter()));
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Coffee espresso = Coffee.
                builder().name("espresso").price(Money.of(CurrencyUnit.of("CNY"),20))
                .createTime(new Date()).updateTime(new Date()).build();
         Coffee latte = Coffee.
                builder().name("latte").price(Money.of(CurrencyUnit.of("CNY"),30))
                .createTime(new Date()).updateTime(new Date()).build();

        coffeeRepository.insert(Arrays.asList(latte,espresso));
        coffeeRepository.findAll(Sort.by("name")).forEach(
                c -> log.info("coffee: {}", c)
        );

        Thread.sleep(1000);
        latte.setPrice(Money.of(CurrencyUnit.of("CNY"),35));
        latte.setUpdateTime(new Date());
        coffeeRepository.save(latte);
        coffeeRepository.findByName("latte").forEach(
                c -> log.info("coffee: {}", c)
        );

        coffeeRepository.deleteAll();
    }
}
