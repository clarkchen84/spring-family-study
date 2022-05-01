package sizhe.chen.spring.controler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sizhe.chen.spring.controler.request.NewOrderRequest;
import sizhe.chen.spring.model.Coffee;
import sizhe.chen.spring.model.CoffeeOrder;
import sizhe.chen.spring.service.CoffeeOrderService;
import sizhe.chen.spring.service.CoffeeService;

@RestController
@Slf4j
@RequestMapping("/")
public class CoffeeOrderController {

    @Autowired
    private CoffeeOrderService coffeeOrderService;
    @Autowired
    private CoffeeService coffeeService;

    @GetMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public CoffeeOrder getOrder(@PathVariable Long id){
        return coffeeOrderService.get(id);
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public CoffeeOrder create(@RequestBody NewOrderRequest orderRequest){
        log.info("Recieve new Order {}", orderRequest);
        Coffee[] coffeeList = coffeeService.getCoffeeByName(orderRequest.getItmes()).toArray(new Coffee[0]);
        return coffeeOrderService.createOrder(orderRequest.getCustomer(),coffeeList);
    }


}
