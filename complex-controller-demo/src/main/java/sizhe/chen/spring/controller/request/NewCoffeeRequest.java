package sizhe.chen.spring.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.money.Money;
import org.springframework.format.number.money.MonetaryAmountFormatter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class NewCoffeeRequest {

    @NotEmpty
    private String name;

    @NotNull
    private Money price;
}
