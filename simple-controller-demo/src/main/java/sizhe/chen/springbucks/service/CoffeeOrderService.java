package sizhe.chen.springbucks.service;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sizhe.chen.springbucks.model.Coffee;
import sizhe.chen.springbucks.model.CoffeeOrder;
import sizhe.chen.springbucks.model.OrderState;
import sizhe.chen.springbucks.repository.CoffeeOrderRepository;

import java.util.Arrays;

@Service
@Slf4j
public class CoffeeOrderService {
    @Autowired
    private CoffeeOrderRepository coffeeOrderRepository;

    public CoffeeOrder createCoffee(String customer, Coffee... coffees){
        CoffeeOrder coffeeOrder = CoffeeOrder.builder().customer(customer)
                .items(Arrays.asList(coffees))
                .state(OrderState.INIT)
                .build();

       CoffeeOrder saved = coffeeOrderRepository.save(coffeeOrder);

       log.info("New Order : {}" , saved);

       return saved;
    }

    public boolean updateOrder(CoffeeOrder order, OrderState state){

        if(state.compareTo(order.getState()) < 0){

            log.warn("Wrong State order: {},{}",state, order.getState());
            return  false;
        }

        order.setState(state);
        coffeeOrderRepository.save(order);
        log.info("Update Order: {}", order);

        return true;

    }
}
