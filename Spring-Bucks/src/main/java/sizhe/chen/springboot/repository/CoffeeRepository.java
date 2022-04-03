package sizhe.chen.springboot.repository;

import org.springframework.data.repository.CrudRepository;
import sizhe.chen.springboot.domain.Coffee;

public interface CoffeeRepository extends BaseRepository<Coffee,Long> {
}
