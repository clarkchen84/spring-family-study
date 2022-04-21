package sizhe.chen.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sizhe.chen.redis.model.Coffee;
import sizhe.chen.redis.model.CoffeeOrder;
import sizhe.chen.redis.model.OrderState;
import sizhe.chen.redis.repository.CoffeeOrderRepository;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @Author: sizhe.chen
 * @Date: Create in 11:40 上午 2022/4/17
 * @Description:
 * @Modified:
 * @Version:
 */
@Service
@Slf4j
public class CoffeeOrderService {
    @Autowired
    private CoffeeOrderRepository orderRepository;

    public CoffeeOrder createOrder(String customer, Coffee...coffee) {
        CoffeeOrder order = CoffeeOrder.builder()
                .customer(customer)
                .items(new ArrayList<Coffee>(Arrays.asList(coffee)))
                .state(OrderState.INIT)
                .build();
        CoffeeOrder saved = orderRepository.save(order);
        log.info("New Order: {}", saved);
        return saved;
    }

    public boolean updateState(CoffeeOrder order, OrderState state) {
        if (state.compareTo(order.getState()) <= 0) {
            log.warn("Wrong State order: {}, {}", state, order.getState());
            return false;
        }
        order.setState(state);
        orderRepository.save(order);
        log.info("Updated Order: {}", order);
        return true;
    }
}
