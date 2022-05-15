package sizhe.chen.model;

import lombok.*;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Author: sizhe.chen
 * @Date: Create in 2:42 下午 2022/5/15
 * @Description:
 * @Modified:
 * @Version:
 */
@Entity
@Table(name="t_coffee")
@Builder
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Coffee extends BaseEntity implements Serializable {

    private String name;
    @Type(type="org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmount",
        parameters = {@Parameter(name="currencyCode",value = "CNY")}
    )
    private Money price;

}
