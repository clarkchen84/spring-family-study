package sizhe.chen.data.mongo;

import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import sizhe.chen.data.mongo.converter.MoneyReadConverter;
import sizhe.chen.data.mongo.model.Coffee;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@Slf4j
public class MongoDbApplicationBootStrap implements ApplicationRunner {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public MongoCustomConversions mongoCustomConversions(){
        return new MongoCustomConversions(Arrays.asList(new MoneyReadConverter()));
    }


    public static void main(String[] args) {
        SpringApplication.run(MongoDbApplicationBootStrap.class,args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Coffee coffee = Coffee.
                builder().name("espresso").price(Money.of(CurrencyUnit.of("CNY"),20))
                .createTime(new Date()).updateTime(new Date()).build();
        Coffee saved = mongoTemplate.save(coffee);

        log.info("Coffee : {}" , saved);
        List<Coffee> list = mongoTemplate
                .find(Query
                        .query(Criteria.where("name").is("espresso")),Coffee.class);

        log.info("find {} coffee:" ,list);

        Thread.sleep(1000);

        UpdateResult updateResult = mongoTemplate.updateFirst(
                Query.query(Criteria.where("name").is("espresso")),
                new Update().set("price",Money.of(CurrencyUnit.of("CNY"),
                                30)).currentDate("updateTime"),
                Coffee.class);
        log.info("Update Result: {}", updateResult.getModifiedCount());

        Coffee updateOne = mongoTemplate.findById(saved.getId(), Coffee.class);
        log.info("Update Result: {}", updateOne);

        mongoTemplate.remove(updateOne);
    }
}
