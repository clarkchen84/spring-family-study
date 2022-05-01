package sizhe.chen.spring.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class NewCoffeeOrderRequest {
    private String customer;
    private List<String> items;
}
