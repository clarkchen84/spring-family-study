package sizhe.chen.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import sizhe.chen.cache.service.CoffeeOrderService;
import sizhe.chen.cache.service.CoffeeService;

@SpringBootApplication
@Slf4j
@EnableJpaRepositories
@EnableTransactionManagement
@EnableCaching(proxyTargetClass = true)
public class CacheApplicationBootStrap implements ApplicationRunner {

    @Autowired
    private CoffeeService coffeeService;
    @Autowired
    private CoffeeOrderService coffeeOrderService;

    public static void main(String[] args) {
        SpringApplication.run(CacheApplicationBootStrap.class,args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Count: {}", coffeeService.findAllCoffee().size());
        for (int i = 0; i < 10; i++) {
            log.info("Reading from cache.");
            coffeeService.findAllCoffee();
        }
        coffeeService.reloadCoffee();
        log.info("Reading after refresh.");
        coffeeService.findAllCoffee().forEach(c -> log.info("Coffee {}", c.getName()));

    }
}
