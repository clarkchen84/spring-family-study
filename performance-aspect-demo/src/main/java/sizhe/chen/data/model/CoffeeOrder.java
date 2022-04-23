package sizhe.chen.data.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: sizhe.chen
 * @Date: Create in 10:12 上午 2022/4/23
 * @Description:
 * @Modified:
 * @Version:
 */
@Entity
@Table(name = "t_order")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class CoffeeOrder extends BaseEntity implements Serializable {

    private String customer;

    @OrderBy("id")
    @ManyToMany
    @JoinTable(name = "t_order_coffee")
    private List<Coffee> items;

    @Enumerated
    @Column(nullable = false)
    private OrderState state;
}
