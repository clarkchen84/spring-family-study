package sizhe.chen.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: sizhe.chen
 * @Date: Create in 10:29 下午 2022/5/15
 * @Description:
 * @Modified:
 * @Version:
 */
@Entity
@Table(name = "t_order")
@Builder
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CoffeeOrder extends BaseEntity implements Serializable {
    private String customer;
    @ManyToMany
    @JoinTable(name="t_order_coffee")
    @OrderBy("id")
    private List<Coffee> items;

    @Enumerated
    @Column(nullable = false)
    private OrderState state;
}
