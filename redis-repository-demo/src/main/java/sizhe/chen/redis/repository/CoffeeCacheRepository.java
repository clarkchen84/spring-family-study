package sizhe.chen.redis.repository;

import org.springframework.data.repository.CrudRepository;
import sizhe.chen.redis.model.CoffeeCache;

import java.util.Optional;

/**
 * @Author: sizhe.chen
 * @Date: Create in 11:31 上午 2022/4/17
 * @Description:
 * @Modified:
 * @Version:
 */

public interface CoffeeCacheRepository extends CrudRepository<CoffeeCache,Long> {
    Optional<CoffeeCache> findOneByName(String name);
}

