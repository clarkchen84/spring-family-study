package sizhe.chen.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sizhe.chen.spring.controller.request.NewCoffeeRequest;
import sizhe.chen.spring.model.Coffee;
import sizhe.chen.spring.service.CoffeeService;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/coffee")
@Slf4j
public class CoffeeController {
    @Autowired
    private CoffeeService coffeeService;

    @PostMapping(path = "/" ,consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public  Coffee addCoffee(@Validated NewCoffeeRequest request, BindingResult result){
        if(result.hasErrors()){
            log.error("BindingErrors :{}" , result);
            return null;
        }
        Coffee coffee = coffeeService.saveCoffee(request.getName(),request.getPrice());
        return coffee;
    }

    @PostMapping(path = "/" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public List<Coffee> batchAddCoffee(@RequestParam("file") MultipartFile multipartFile){
        List<Coffee> coffees = new ArrayList<>();
        if(!multipartFile.isEmpty()){
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()));
                String str;
                while ((str = reader.readLine()) != null ){
                    String[] arr = StringUtils.split(str, " ");
                    if(arr != null && arr.length == 2){
                        coffees.add(coffeeService.saveCoffee(arr[0], Money.of(CurrencyUnit.of("CNY"),
                                NumberUtils.createBigDecimal(arr[1]))));
                    }
                }
            } catch (IOException e) {
                log.error("exception :{}"+ e.getMessage());
            }finally {
                IOUtils.closeQuietly(reader);
            }
        }
        return coffees;
    }


    @GetMapping(path="/", params = "!name")
    @ResponseBody
    public List<Coffee>  getAll(){
        return coffeeService.getCoffeeAll();
    }

    @GetMapping(path = "/", params = "name")
    @ResponseBody
    public Coffee getByName(@RequestParam String name){
        return coffeeService.getCoffee(name);
    }

    @GetMapping(path = "/{id}" ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Coffee getById(@PathVariable Long id){
        Coffee coffee = coffeeService.getCoffee(id);
        return coffee;
    }

}
