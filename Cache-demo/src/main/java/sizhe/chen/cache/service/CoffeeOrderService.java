package sizhe.chen.cache.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sizhe.chen.cache.model.Coffee;
import sizhe.chen.cache.model.CoffeeOrder;
import sizhe.chen.cache.model.OrderState;
import sizhe.chen.cache.repository.CoffeeOrderRepository;

import java.util.Arrays;

@Service
@Transactional
@Slf4j
public class CoffeeOrderService {
    @Autowired
    private CoffeeOrderRepository coffeeOrderRepository;

    public CoffeeOrder createCoffeeOrder(String customer, Coffee... coffees){
        CoffeeOrder order = CoffeeOrder.builder().customer(customer).items(Arrays.asList(coffees))
                .state(OrderState.INIT).build();
        CoffeeOrder saved = coffeeOrderRepository.save(order);
        log.info("create order : {}", order);
        return order;
    }
    public boolean updateCoffeeOrder(OrderState state, CoffeeOrder order){

        if(state.compareTo(order.getState()) < 0){
            log.warn("Wrong State order : {} -{}",state,order);
        }
        order.setState(state);
        coffeeOrderRepository.save(order);
        log.info("Update Order :{}", order);
        return true;
    }


}
