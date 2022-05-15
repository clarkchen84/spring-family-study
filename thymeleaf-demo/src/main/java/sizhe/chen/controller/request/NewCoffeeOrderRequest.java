package sizhe.chen.controller.request;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import sizhe.chen.model.OrderState;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author: sizhe.chen
 * @Date: Create in 3:38 下午 2022/5/15
 * @Description:
 * @Modified:
 * @Version:
 */

@Data
@Builder
@ToString
public class NewCoffeeOrderRequest {
    @NotEmpty
    private String customer;
    @NotEmpty
    private List<String> items;
}
