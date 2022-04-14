package sizhe.chen.redis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sizhe.chen.redis.model.Coffee;

public interface CoffeeRepository extends JpaRepository<Coffee,Long> {
}
