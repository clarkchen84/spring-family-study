package sizhe.chen.redis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sizhe.chen.redis.model.CoffeeOrder;

/**
 * @Author: sizhe.chen
 * @Date: Create in 11:34 上午 2022/4/17
 * @Description:
 * @Modified:
 * @Version:
 */

public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder, Long> {
}
