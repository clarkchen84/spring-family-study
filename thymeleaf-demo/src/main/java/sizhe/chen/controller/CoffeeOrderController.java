package sizhe.chen.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    @ResponseBody
    private CoffeeOrder getOrder(@PathVariable("id") Long id){
        return coffeeOrderService.getOrder(id);
    }

    @PostMapping(value = "/",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CoffeeOrder createOrder(@Validated NewCoffeeOrderRequest request){
        List<Coffee> coffees = coffeeService.getCoffeeByName(request.getItems());

        //CoffeeOrder coffeeOrder = CoffeeOrder.builder().items(coffees).customer(request.getCustomer()).state(OrderState.INIT).build();
        return coffeeOrderService.createOrder(request.getCustomer(),coffees.toArray(new Coffee[0]));
    }

    @ModelAttribute
    public List<Coffee> coffeeList(){
        return coffeeService.getAllCoffee();
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createOrder(@Validated NewCoffeeOrderRequest order , BindingResult result, ModelMap modelMap){
        if(result.hasErrors()){
            log.warn("Binding result :{}", result);
            modelMap.addAttribute("message", result.toString());
            return "create-order-form";
        }
        log.info("Receive new Order {}" , order);
        Coffee[] coffee = coffeeService.getCoffeeByName(order.getItems()).toArray(new Coffee[0]);
        CoffeeOrder coffeeOrder = coffeeOrderService.createOrder(order.getCustomer(),coffee);
        return "redirect:/order/" + coffeeOrder.getId();

    }

    @GetMapping(params = "/")
    public ModelAndView showCreateForm(){
        return new ModelAndView("create-order-form");
    }
}
