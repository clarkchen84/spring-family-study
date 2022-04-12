package sizhe.chen.jedis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sizhe.chen.jedis.model.Coffee;
import sizhe.chen.jedis.model.CoffeeOrder;
import sizhe.chen.jedis.model.OrderState;
import sizhe.chen.jedis.repository.CoffeeOrderRepository;

import java.util.Arrays;

@Service
@Slf4j
@Transactional
public class CoffeeOrderService {

    @Autowired
    private CoffeeOrderRepository repository;

    public CoffeeOrder createOrder(String customer, Coffee... coffees){
        CoffeeOrder order = CoffeeOrder.builder().customer(customer).items(Arrays.asList(coffees))
                .state(OrderState.INIT).build();
       order =   repository.save(order);
       log.info("New Order {}", order);
       return order;
    }

    public boolean updateState(CoffeeOrder order , OrderState state){
        if(state.compareTo(order.getState()) < 0){
            log.warn("Wrong state order:{},{}",order, state);
            return false;
        }

        order.setState(state);
        repository.save(order);
        log.info("Update Order {}" , order);
        return true;
    }
}
