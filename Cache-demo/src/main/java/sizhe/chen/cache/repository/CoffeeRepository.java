package sizhe.chen.cache.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sizhe.chen.cache.model.Coffee;

public interface CoffeeRepository extends JpaRepository<Coffee,Long> {
}
