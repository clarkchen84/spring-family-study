package sizhe.chen.cache.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sizhe.chen.cache.model.CoffeeOrder;

public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder,Long> {
}
