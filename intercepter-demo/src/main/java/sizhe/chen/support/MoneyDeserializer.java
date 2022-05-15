package sizhe.chen.support;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.io.IOException;

/**
 * @Author: sizhe.chen
 * @Date: Create in 10:54 下午 2022/5/15
 * @Description:
 * @Modified:
 * @Version:
 */

public class MoneyDeserializer extends StdDeserializer<Money> {

    protected MoneyDeserializer(){
        super(Money.class);
    }

    @Override
    public Money deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return Money.of(CurrencyUnit.of("CNY"),jsonParser.getDecimalValue());
    }
}
