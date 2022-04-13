package sizhe.chen.cache.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import sizhe.chen.cache.model.Coffee;
import sizhe.chen.cache.repository.CoffeeRepository;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Service
@Slf4j
@CacheConfig(cacheNames = "coffee")
public class CoffeeService  {
    @Autowired
    private CoffeeRepository coffeeRepository;

    @Cacheable
    public List<Coffee> findAllCoffee(){
        log.info("this is findAllCoffee execute");
        return  coffeeRepository.findAll();
    }

    @CacheEvict
    public void reloadCoffee(){

    }

    public Optional<Coffee> findOneCoffee(String name){
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("name",exact().ignoreCase());
        Optional<Coffee> coffee = coffeeRepository.findOne(
                Example.of(Coffee.builder().name(name).build(),exampleMatcher)
        );
        log.info("Find Coffee: {}",coffee);
        return coffee;
    }
}
