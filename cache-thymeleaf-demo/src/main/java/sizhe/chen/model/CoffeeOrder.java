package sizhe.chen.model;

import lombok.*;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: sizhe.chen
 * @Date: Create in 2:54 下午 2022/5/15
 * @Description:
 * @Modified:
 * @Version:
 */
@Entity
@Table(name = "t_order")
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class CoffeeOrder extends  BaseEntity implements Serializable {

    private String customer;

    @ManyToMany
    @OrderBy("id")
    @JoinTable(name="t_order_coffee")
    private List<Coffee> items;

    @Enumerated
    @Column(nullable = false)
    private OrderState state;

}
