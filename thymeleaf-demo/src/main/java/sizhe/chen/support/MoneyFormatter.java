package sizhe.chen.support;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

/**
 * @Author: sizhe.chen
 * @Date: Create in 3:27 下午 2022/5/15
 * @Description:
 * @Modified:
 * @Version:
 */
@Component
public class MoneyFormatter implements Formatter<Money> {
    @Override
    public Money parse(String text, Locale locale) throws ParseException {

        if(NumberUtils.isParsable(text)){
            return Money.of(CurrencyUnit.of("CNY"),NumberUtils.createBigDecimal(text));
        }else if(StringUtils.isNotEmpty(text)){
            String[] split = StringUtils.split(text, " ");
            if(split != null && split.length == 2  && NumberUtils.isParsable(split[1])){
                return Money.of(CurrencyUnit.of(split[0]),NumberUtils.createBigDecimal(split[1]));
            }else{
                throw new ParseException(text,0);
            }
        }
         throw new ParseException(text,0);
    }

    @Override
    public String print(Money object, Locale locale) {
        if(object != null){
            return null;
        }
        return object.getCurrencyUnit() + " " + object.getAmount();
    }
}
