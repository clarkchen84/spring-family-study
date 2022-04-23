package sizhe.chen.data.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import sizhe.chen.data.model.Coffee;
import sizhe.chen.data.repository.CoffeeRepository;

import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;


/**
 * @Author: sizhe.chen
 * @Date: Create in 10:18 上午 2022/4/23
 * @Description:
 * @Modified:
 * @Version:
 */
@Service
@Slf4j
public class CoffeeService {
    @Autowired
    private CoffeeRepository coffeeRepository;

    public Optional<Coffee> findOneCoffee(String name){
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("name",exact().ignoreCase());

        Optional<Coffee> coffee =coffeeRepository.findOne(Example.of(Coffee.builder().name(name).build(),exampleMatcher));
        log.info("Coffee Found :{}" , coffee);
        return  coffee;
    }
}
