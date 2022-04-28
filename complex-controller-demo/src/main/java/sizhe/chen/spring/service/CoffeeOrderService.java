package sizhe.chen.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sizhe.chen.spring.model.Coffee;
import sizhe.chen.spring.model.CoffeeOrder;
import sizhe.chen.spring.model.OrderState;
import sizhe.chen.spring.repository.CoffeeOrderRepository;

import java.util.Arrays;

@Service
@Slf4j
@Transactional
public class CoffeeOrderService {
    @Autowired
    private CoffeeOrderRepository coffeeOrderRepository;

    public CoffeeOrder get(Long id){
        return coffeeOrderRepository.getOne(id);
    }

    public CoffeeOrder createOrder(String customer, Coffee... coffees){
        CoffeeOrder order = CoffeeOrder.builder().
                customer(customer).items(Arrays.asList(coffees)).build();
        CoffeeOrder saved = coffeeOrderRepository.save(order);
        log.info("New Order {}" + saved);
        return saved;

    }

    public boolean updateCoffee(OrderState orderState, CoffeeOrder order){
        if(orderState.compareTo(order.getState()) < 0){
            log.info("Wrong Order state : {},{}" ,orderState, order.getState());
            return  false;
        }

        order.setState(orderState);
        coffeeOrderRepository.save(order);
        log.info("Update order : {}", order);
        return true;
    }
}
