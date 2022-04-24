package sizhe.chen.springbucks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sizhe.chen.springbucks.model.CoffeeOrder;

public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder,Long> {
}
