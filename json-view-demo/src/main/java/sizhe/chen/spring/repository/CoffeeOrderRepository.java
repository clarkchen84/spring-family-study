package sizhe.chen.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sizhe.chen.spring.model.CoffeeOrder;

public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder, Long> {
}
