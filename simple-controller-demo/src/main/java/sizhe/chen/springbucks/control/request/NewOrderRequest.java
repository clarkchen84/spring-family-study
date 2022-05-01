package sizhe.chen.springbucks.control.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sizhe.chen.springbucks.model.Coffee;

import java.util.List;

@Getter
@Setter
@ToString
public class NewOrderRequest {

    private String customer;
    private List<String> items;
}
