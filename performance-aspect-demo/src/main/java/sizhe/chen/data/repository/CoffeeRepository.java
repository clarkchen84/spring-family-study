package sizhe.chen.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sizhe.chen.data.model.Coffee;

/**
 * @Author: sizhe.chen
 * @Date: Create in 10:16 上午 2022/4/23
 * @Description:
 * @Modified:
 * @Version:
 */

public interface CoffeeRepository extends JpaRepository<Coffee,Long> {
}
