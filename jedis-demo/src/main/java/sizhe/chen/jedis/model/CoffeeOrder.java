package sizhe.chen.jedis.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString(callSuper = true)
@Table(name = "t_order")
public class CoffeeOrder extends  BaseEntity implements Serializable {

    private String customer;
    @ManyToMany
    @JoinTable(name = "t_order_coffee")
    @OrderBy("id")
    private List<Coffee>  items;

    @Enumerated
    @Column(nullable = false)
    private OrderState state;
}
