package sizhe.chen.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sizhe.chen.controller.request.NewCoffeeRequest;
import sizhe.chen.model.Coffee;
import sizhe.chen.service.CoffeeService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: sizhe.chen
 * @Date: Create in 3:35 下午 2022/5/15
 * @Description:
 * @Modified:
 * @Version:
 */
@Controller
@RequestMapping("/coffee")
@Slf4j
public class CoffeeController {
    @Autowired
    private CoffeeService coffeeService;

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Coffee addCoffee(@Validated  NewCoffeeRequest coffeeRequest, BindingResult result){
        if(result.hasErrors()){
            log.warn("Binding Errors:{}",result);
            return null;
        }
        return coffeeService.saveCoffee(coffeeRequest.getName(),coffeeRequest.getPrice());
    }

    @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus
    public List<Coffee> batchAddCoffee(@RequestParam("file")MultipartFile file){
        List<Coffee> coffees = new ArrayList<>();

        if(!file.isEmpty()){
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
                String str;
                while((str = reader.readLine()) != null){
                    String[] split = StringUtils.split(str, " ");
                    if (split != null && split.length == 2) {
                        coffees.add(coffeeService.saveCoffee(split[0],
                                Money.of(CurrencyUnit.of("CNY"),
                                        NumberUtils.createBigDecimal(split[1]))));
                    }
                }
            } catch (IOException e) {
                log.warn(e.getMessage());
            }finally {
                IOUtils.closeQuietly(reader);
            }
        }
        return  coffees;
    }

    @GetMapping(path = "/", params = "!name")
    @ResponseBody
    public List<Coffee> getAll(){
        return coffeeService.getAllCoffee();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Coffee findById(@PathVariable Long id){
        Coffee coffee = coffeeService.getCoffee(id);
        return coffee;
    }

    @GetMapping(value = "/", params = "name")
    @ResponseBody
    public Coffee findByName(@RequestParam( value = "name") String name){
        return coffeeService.getCoffee(name);
    }
}
