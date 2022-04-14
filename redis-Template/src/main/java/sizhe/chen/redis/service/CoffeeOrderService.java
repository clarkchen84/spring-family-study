package sizhe.chen.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sizhe.chen.redis.model.Coffee;
import sizhe.chen.redis.model.CoffeeOrder;
import sizhe.chen.redis.model.OrderState;
import sizhe.chen.redis.repository.CoffeeOrderRepository;

import java.util.Arrays;

@Service
@Slf4j
public class CoffeeOrderService {
    @Autowired
    private CoffeeOrderRepository coffeeOrderRepository;
    public CoffeeOrder createCoffeeOrder(String customer, Coffee...coffees){
        CoffeeOrder coffeeOrder = CoffeeOrder.builder().customer(customer)
                .state(OrderState.INIT)
                .items(Arrays.asList(coffees))
                .build();

        CoffeeOrder saved = coffeeOrderRepository.save(coffeeOrder);
        log.info("New Order :{} " , saved );
        return  saved;
    }

    public boolean updateSate(CoffeeOrder coffeeOrder ,OrderState state){
        if(state.compareTo(coffeeOrder.getState()) < 0){
            log.warn("Wrong State order: {}, {}", state, coffeeOrder.getState());
            return false;
        }
        coffeeOrder.setState(state);
        coffeeOrderRepository.save(coffeeOrder);
        log.info("Updated Order: {}", coffeeOrder);
        return true;
    }
}
