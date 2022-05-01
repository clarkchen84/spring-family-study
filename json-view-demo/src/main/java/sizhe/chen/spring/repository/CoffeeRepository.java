package sizhe.chen.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sizhe.chen.spring.model.Coffee;

import java.util.List;

public interface CoffeeRepository  extends JpaRepository<Coffee,Long> {
     List<Coffee>  findByNameInOrderById(List<String> names);

     Coffee findByName(String name );

}
