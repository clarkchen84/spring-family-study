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
 * @Date: Create in 10:42 下午 2022/5/15
 * @Description:
 * @Modified:
 * @Version:
 */
@Service
@Slf4j
@Transactional
public class CoffeeOrderService {
    @Autowired
    private CoffeeOrderRepository repository;

    public CoffeeOrder get(Long id){
        return repository.getOne(id);
    }

    public CoffeeOrder createOrder(String customer, Coffee... coffees){
        CoffeeOrder coffeeOrder = CoffeeOrder.builder().customer(customer).items(Arrays.asList(coffees)).state(OrderState.INIT).build();
        CoffeeOrder saved =repository.save(coffeeOrder);

        log.info("new Order : {}",saved);
        return  saved;
    }

    public boolean updateSaved(CoffeeOrder order, OrderState state){
        if(state.compareTo(order.getState()) <= 0){
            log.warn("Wrong state order: {}.{}" ,state, order.getState());
            return false;
        }
        order.setState(state);
        repository.save(order);
        log.info("Update Order {} ",order);
        return true;
    }
}
