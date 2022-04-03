package sizhe.chen.springboot;

import lombok.extern.slf4j.Slf4j;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import sizhe.chen.springboot.domain.Coffee;
import sizhe.chen.springboot.domain.CoffeeOrder;
import sizhe.chen.springboot.domain.OderState;
import sizhe.chen.springboot.repository.CoffeeOrderRepository;
import sizhe.chen.springboot.repository.CoffeeRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@Slf4j
public class SpringBucksBootStrap implements CommandLineRunner {
    @Autowired
    private CoffeeRepository coffeeRepository;

    @Autowired
    private CoffeeOrderRepository coffeeOrderRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringBucksBootStrap.class,args);
    }

    private void initOrder(){
        Coffee espresso = Coffee.builder().name("espresso")
                .price(Money.of(CurrencyUnit.of("CNY"),20.0))
                .build();
        coffeeRepository.save(espresso);
        log.info("coffee: {}" ,espresso);

        Coffee latte = Coffee.builder().name("latte")
                .price(Money.of(CurrencyUnit.of("CNY"),30.0))
                .build();
        coffeeRepository.save(latte);
        log.info("coffee: {}" ,latte);

        CoffeeOrder order = CoffeeOrder.builder()
                .customer("Li lei")
                .items(Collections.singletonList(espresso))
                .state(OderState.INIT)
                .build();

        coffeeOrderRepository.save(order);
        log.info("coffeeOrder: {}" ,order);

        order = CoffeeOrder.builder()
                .customer("Li lei")
                .items(List.of(espresso,latte))
                .state(OderState.INIT)
                .build();

        coffeeOrderRepository.save(order);
        log.info("coffeeOrder: {}" ,order);


    }
    private void findOrders(){
        coffeeRepository
                .findAll(Sort.by(Sort.Direction.DESC,"id") )
                .forEach(
                        coffee -> {
                            log.info("loading: {}" , coffee);
                        }
                );

        List<CoffeeOrder> coffeeOrders= coffeeOrderRepository
                .findTop3ByOrderByUpdateTimeDescIdAsc();

        log.info("findTop3ByOrderByUpdateTimeDescIdAsc: {}" ,getJoinedOrderId(coffeeOrders));

        coffeeOrders = coffeeOrderRepository.findByCustomerOrderById("Li lei");
        log.info("findByCustomerOrderById: {}", coffeeOrders);

        coffeeOrders.forEach(o -> {
            log.info("Order: {}" , o.getId());
            o.getItems().forEach(i -> log.info("item :{}" , i));
        });

        coffeeOrders = coffeeOrderRepository.findByItems_Name("latte");
        log.info("findByItems_Name: {}", getJoinedOrderId(coffeeOrders));


    }
    private String getJoinedOrderId(List<CoffeeOrder> list){
        return list.stream()
                .map(o -> o.getId().toString())
                .collect(Collectors.joining());
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initOrder();
        findOrders();
    }
}
