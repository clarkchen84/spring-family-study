package sizhe.chen.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import sizhe.chen.mongo.model.Coffee;

import java.util.List;

public interface CoffeeRepository extends MongoRepository<Coffee,String> {

    List<Coffee> findByName(String name);
}
