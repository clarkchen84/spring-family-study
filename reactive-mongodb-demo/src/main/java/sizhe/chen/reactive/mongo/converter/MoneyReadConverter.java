package sizhe.chen.reactive.mongo.converter;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.core.convert.converter.Converter;



/**
 * @Author: sizhe.chen
 * @Date: Create in 8:03 下午 2022/4/22
 * @Description:
 * @Modified:
 * @Version:
 */

public class MoneyReadConverter implements Converter<Long,Money> {

    @Override
    public Money convert(Long aLong) {
        return Money.ofMinor(CurrencyUnit.of("CNY"), aLong);
    }
}
