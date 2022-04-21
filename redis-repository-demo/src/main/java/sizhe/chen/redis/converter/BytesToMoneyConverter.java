package sizhe.chen.redis.converter;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.nio.charset.StandardCharsets;

/**
 * @Author: sizhe.chen
 * @Date: Create in 11:26 上午 2022/4/17
 * @Description:
 * @Modified:
 * @Version:
 */

@ReadingConverter
public class BytesToMoneyConverter implements Converter<byte[], Money> {
    @Override
    public Money convert(byte[] bytes) {
        String value = new String(bytes, StandardCharsets.UTF_8);
        return Money.ofMinor(CurrencyUnit.of("CNY"),Long.parseLong(value));
    }
}
