package sizhe.chen.springbucks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sizhe.chen.springbucks.model.Coffee;

import java.util.List;

public interface CoffeeRepository extends JpaRepository<Coffee,Long> {

    List<Coffee> findCoffeeByNameInOrderById(List<String> list);
}
