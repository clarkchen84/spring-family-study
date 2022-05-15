package sizhe.chen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sizhe.chen.model.Coffee;

import java.util.List;

/**
 * @Author: sizhe.chen
 * @Date: Create in 10:32 下午 2022/5/15
 * @Description:
 * @Modified:
 * @Version:
 */

public interface CoffeeRepository extends JpaRepository<Coffee,Long> {
    List<Coffee> findByNameInOrderById(List<String> names);
    Coffee findByName(String name);
}
