package sizhe.chen.jedis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sizhe.chen.jedis.model.Coffee;

public interface CoffeeRepository extends JpaRepository<Coffee,Long> {

}
