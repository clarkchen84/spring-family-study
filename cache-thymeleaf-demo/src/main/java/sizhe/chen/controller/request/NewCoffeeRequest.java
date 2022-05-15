package sizhe.chen.controller.request;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.joda.money.Money;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: sizhe.chen
 * @Date: Create in 3:37 下午 2022/5/15
 * @Description:
 * @Modified:
 * @Version:
 */
@Data
@Builder
@ToString
public class NewCoffeeRequest {
    @NotEmpty
    private String name;

    @NotNull
    private Money price;
}
