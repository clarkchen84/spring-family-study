package sizhe.chen.resttemplate.support;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.joda.money.Money;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * @Author: sizhe.chen
 * @Date: Create in 3:15 下午 2022/5/22
 * @Description:
 * @Modified:
 * @Version:
 */
@JsonComponent
public class MoneySerializer extends StdSerializer<Money> {


    protected MoneySerializer() {
        super(Money.class);
    }

    @Override
    public void serialize(Money o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(o.getAmount());
    }
}
