package sizhe.chen.data.model;

import lombok.*;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Author: sizhe.chen
 * @Date: Create in 10:06 上午 2022/4/23
 * @Description:
 * @Modified:
 * @Version:
 */
@Entity
@Table(name = "t_coffee")
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Coffee extends BaseEntity implements Serializable {
    private String name;

    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmount",
        parameters = {@Parameter(name = "currencyCode",value="CNY")}
    )
    private Money price;
}
