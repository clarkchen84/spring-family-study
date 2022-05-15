package sizhe.chen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sizhe.chen.model.CoffeeOrder;

/**
 * @Author: sizhe.chen
 * @Date: Create in 10:33 下午 2022/5/15
 * @Description:
 * @Modified:
 * @Version:
 */

public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder,Long> {
}
