package sizhe.chen.redis.model;

import lombok.*;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: sizhe.chen
 * @Date: Create in 11:14 上午 2022/4/17
 * @Description:
 * @Modified:
 * @Version:
 */
@Entity
@Table(name = "t_order")
@Data
@Builder
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class CoffeeOrder extends BaseEntity implements Serializable {
    @Column(nullable = false)
    private OrderState state;
    private String customer;
    @OrderBy("id")

    @ManyToMany
    @JoinColumn(name = "t_order_coffee")
    private List<Coffee> items;
}
