package sizhe.chen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sizhe.chen.model.Coffee;
import sizhe.chen.model.CoffeeOrder;
import sizhe.chen.model.OrderState;
import sizhe.chen.repository.CoffeeOrderRepository;

import java.util.Arrays;

/**
 * @Author: sizhe.chen
 * @Date: Create in 3:18 下午 2022/5/15
 * @Description:
 * @Modified:
 * @Version:
 */

@Service
@Transactional
@Slf4j
public class CoffeeOrderService {

    @Autowired
    private CoffeeOrderRepository coffeeOrderRepository;

    public CoffeeOrder getOrder(Long id){
        return coffeeOrderRepository.getOne(id);
    }

    public CoffeeOrder createOrder(String customer, Coffee... coffee ){
        CoffeeOrder coffeeOrder = CoffeeOrder.builder().customer(customer)
                .items(Arrays.asList(coffee))
                .state(OrderState.INIT)
                .build();
        CoffeeOrder saved = coffeeOrderRepository.save(coffeeOrder);
        log.info("New Order : {}",saved);
        return  saved;
    }

    public boolean updateOrder(CoffeeOrder order, OrderState state){
        if(state.compareTo(order.getState()) <= 0){
            log.warn("Wrong order state {} : {}", state, order.getState());
            return false;
        }

        order.setState(state);
        coffeeOrderRepository.save(order);
        log.info("Updated order : {}" , order);
        return true;
    }
}
