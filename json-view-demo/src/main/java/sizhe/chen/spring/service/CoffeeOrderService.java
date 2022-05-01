package sizhe.chen.spring.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sizhe.chen.spring.model.Coffee;
import sizhe.chen.spring.model.CoffeeOrder;
import sizhe.chen.spring.model.OrderState;
import sizhe.chen.spring.repository.CoffeeOrderRepository;
import sizhe.chen.spring.repository.CoffeeRepository;

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
        CoffeeOrder coffeeOrder = CoffeeOrder.builder().customer(customer)
                .items(Arrays.asList(coffees)).state(OrderState.INIT).build();

        CoffeeOrder saved = coffeeOrderRepository.save(coffeeOrder);
        log.info("New Coffee Order : {}" , saved);
        return saved;
    }

    public boolean updateOrder(OrderState state, CoffeeOrder order){
        if(state.compareTo(order.getState()) < 0){
            log.error("Wrong order state {}:{} " ,state, order);
            return false;
        }

        order.setState(state);
        coffeeOrderRepository.save(order);
        log.info("Update Order :{}", order);
        return true;
    }

}
