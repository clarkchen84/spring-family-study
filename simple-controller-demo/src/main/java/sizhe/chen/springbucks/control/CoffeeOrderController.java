package sizhe.chen.springbucks.control;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sizhe.chen.springbucks.control.request.NewOrderRequest;
import sizhe.chen.springbucks.model.Coffee;
import sizhe.chen.springbucks.model.CoffeeOrder;
import sizhe.chen.springbucks.service.CoffeeOrderService;
import sizhe.chen.springbucks.service.CoffeeService;

@RestController
@RequestMapping("order")
@Slf4j
public class CoffeeOrderController {

    @Autowired
    private CoffeeOrderService coffeeOrderService;
    @Autowired
    private CoffeeService coffeeService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public CoffeeOrder create(@RequestBody NewOrderRequest newOrder){
        log.info("Receive new Order {}", newOrder);
        Coffee[] coffeeList=coffeeService.getCoffeeByName(newOrder.getItems()).toArray(new Coffee[0]);

        return coffeeOrderService.createCoffee(newOrder.getCustomer(),coffeeList);
    }
}
