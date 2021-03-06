package sizhe.chen.redis.model;

import lombok.*;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "t_coffee")
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Data
@Builder
public class Coffee extends  BaseEntity implements Serializable {
    private String name;
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmount",
    parameters = {@Parameter(name="currencyCode",value="CNY")})
    private Money price;
}
