package sizhe.chen.resttemplate.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.joda.money.Money;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * @Author: sizhe.chen
 * @Date: Create in 4:34 下午 2022/5/22
 * @Description:
 * @Modified:
 * @Version:
 */
@JsonComponent
public class MoneySerializer extends StdSerializer<Money> {
    public MoneySerializer(){
        super(Money.class);
    }

    @Override
    public void serialize(Money money, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(money.getAmount());
    }
}
