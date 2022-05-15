package sizhe.chen.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sizhe.chen.controller.request.NewCoffeeOrderRequest;
import sizhe.chen.model.Coffee;
import sizhe.chen.model.CoffeeOrder;
import sizhe.chen.model.OrderState;
import sizhe.chen.service.CoffeeOrderService;
import sizhe.chen.service.CoffeeService;

import java.util.List;

/**
 * @Author: sizhe.chen
 * @Date: Create in 3:56 下午 2022/5/15
 * @Description:
 * @Modified:
 * @Version:
 */
@Controller
@RequestMapping("/order")
@Slf4j
public class CoffeeOrderController {
    @Autowired
    private CoffeeOrderService coffeeOrderService;

    @Autowired
    private CoffeeService coffeeService;
    @GetMapping("/{id}")
    public CoffeeOrder getOrder(@PathVariable("id") Long id) {
        return coffeeOrderService.getOrder(id);
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CoffeeOrder create(@RequestBody NewCoffeeOrderRequest newOrder) {
        log.info("Receive new Order {}", newOrder);
        Coffee[] coffeeList = coffeeService.getCoffeeByName(newOrder.getItems())
                .toArray(new Coffee[] {});
        return coffeeOrderService.createOrder(newOrder.getCustomer(), coffeeList);
    }
}
