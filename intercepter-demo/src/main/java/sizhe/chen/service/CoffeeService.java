package sizhe.chen.service;

import lombok.extern.slf4j.Slf4j;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sizhe.chen.model.Coffee;
import sizhe.chen.repository.CoffeeRepository;

import java.util.List;

/**
 * @Author: sizhe.chen
 * @Date: Create in 10:36 下午 2022/5/15
 * @Description:
 * @Modified:
 * @Version:
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "CoffeeCaches")
public class CoffeeService {
    @Autowired
    private CoffeeRepository coffeeRepository;

    public Coffee saveCoffee(String name, Money price){
        return coffeeRepository.save(Coffee.builder().name(name).price(price).build());
    }

    public List<Coffee> getAllCoffee(){
        return coffeeRepository.findAll(Sort.by("id"));
    }

    public Coffee getCoffee(Long id){
        return coffeeRepository.getOne(id);
    }

    public Coffee getCoffee(String name){
        return coffeeRepository.findByName(name);
    }

    public List<Coffee> getCoffeeByName(List<String> name){
        return coffeeRepository.findByNameInOrderById(name);
    }



}
