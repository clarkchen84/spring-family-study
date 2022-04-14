package sizhe.chen.redis.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "t_order")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString(callSuper = true)
public class CoffeeOrder extends BaseEntity implements Serializable {
    private String customer;
    @Enumerated
    @Column(nullable = false)
    private OrderState state;
    @ManyToMany
    @JoinTable(name = "t_coffee_order")
    private List<Coffee> items;
}
