package sizhe.chen.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sizhe.chen.redis.model.Coffee;
import sizhe.chen.redis.repository.CoffeeRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Slf4j
@Service
public class CoffeeService {
    private static final String CACHE="SpringBucks-coffee";
    @Autowired
    private CoffeeRepository repository;

    @Autowired
    private RedisTemplate<String ,Coffee> redisTemplate;

    public List<Coffee> findAllCoffees(){
        return repository.findAll();
    }

    public Optional<Coffee> findOneCoffee(String name){
        HashOperations<String,String,Coffee> hashOperations
                = redisTemplate.opsForHash();
        if(redisTemplate.hasKey(CACHE) && hashOperations.hasKey(CACHE,name)){
            log.info("Get coffee: {} fromm redis ", name);
            return Optional.of(hashOperations.get(CACHE,name)) ;
        }
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("name", exact().ignoreCase());
        Optional<Coffee> coffee = repository.findOne(Example.of(
                Coffee.builder().name(name).build(),exampleMatcher
        ));
        log.info("Coffee find {}" ,coffee);

        if(coffee.isPresent()){
            log.info("put coffee {} to redis" ,coffee);
            hashOperations.put(CACHE,name,coffee.get());
            redisTemplate.expire(CACHE,1, TimeUnit.MINUTES);
        }
        return coffee;
    }
}
