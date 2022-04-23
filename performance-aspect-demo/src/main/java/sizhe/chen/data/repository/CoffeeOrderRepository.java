package sizhe.chen.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sizhe.chen.data.model.Coffee;
import sizhe.chen.data.model.CoffeeOrder;

/**
 * @Author: sizhe.chen
 * @Date: Create in 10:17 上午 2022/4/23
 * @Description:
 * @Modified:
 * @Version:
 */

public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder,Long> {

}
