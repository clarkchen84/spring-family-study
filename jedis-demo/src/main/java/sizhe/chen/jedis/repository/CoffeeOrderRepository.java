package sizhe.chen.jedis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sizhe.chen.jedis.model.CoffeeOrder;

public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder,Long> {
}
