package sizhe.chen.redis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sizhe.chen.redis.model.CoffeeOrder;

public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder,Long> {

}
