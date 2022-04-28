package sizhe.chen.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sizhe.chen.spring.controller.request.NewCoffeeOrderRequest;
import sizhe.chen.spring.model.Coffee;
import sizhe.chen.spring.model.CoffeeOrder;
import sizhe.chen.spring.service.CoffeeOrderService;
import sizhe.chen.spring.service.CoffeeService;

import java.util.List;

@Controller
@RequestMapping("/order")
@Slf4j
public class CoffeeOrderController {
    @Autowired
    private CoffeeOrderService coffeeOrderService;
    @Autowired
    private CoffeeService coffeeService;

    @GetMapping("/{id}")
    @ResponseBody
    public CoffeeOrder getOrder(@PathVariable("id") Long id){
        return coffeeOrderService.get(id);
    }

    @PostMapping(value = "/",consumes = MediaType.APPLICATION_JSON_VALUE)
    public CoffeeOrder create(@RequestBody NewCoffeeOrderRequest request){
        log.info("Receive new  Order {} ", request);
        List<Coffee> coffees = coffeeService.getCoffeeByName(request.getItems());
        return coffeeOrderService.createOrder(request.getCustomer(),coffees.toArray(new Coffee[0]));
    }


}
