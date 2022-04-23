package sizhe.chen.data.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sizhe.chen.data.model.Coffee;
import sizhe.chen.data.model.CoffeeOrder;
import sizhe.chen.data.model.OrderState;
import sizhe.chen.data.repository.CoffeeOrderRepository;

import java.util.Arrays;

/**
 * @Author: sizhe.chen
 * @Date: Create in 10:25 上午 2022/4/23
 * @Description:
 * @Modified:
 * @Version:
 */
@Service
@Slf4j
public class CoffeeOrderService {
    @Autowired
    private CoffeeOrderRepository coffeeOrderRepository;

    public CoffeeOrder createOrder(String customer, Coffee... coffees){
        CoffeeOrder order = CoffeeOrder.builder()
                .customer(customer)
                .items(Arrays.asList(coffees))
                .state(OrderState.INIT)
                .build();
        CoffeeOrder saved = coffeeOrderRepository.save(order);
        log.info("New Order {}", saved);
        return saved;
    }

    public boolean updateState(CoffeeOrder order, OrderState orderState){
        if(orderState.compareTo(order.getState()) <= 0){
            log.info("Wrong state {} : {}", orderState,order.getState());
            return  false;
        }

        order.setState(orderState);
        coffeeOrderRepository.save(order);
        log.info("Update order: {}" , order);

        return  true;
    }
}
