package sizhe.chen.reactive.mongo.converter;

import org.joda.money.Money;
import org.springframework.core.convert.converter.Converter;

/**
 * @Author: sizhe.chen
 * @Date: Create in 8:05 下午 2022/4/22
 * @Description:
 * @Modified:
 * @Version:
 */

public class MoneyWriteConverter implements Converter<Money,Long> {
    @Override
    public Long convert(Money money) {
        return money.getAmountMinorLong();
    }
}
