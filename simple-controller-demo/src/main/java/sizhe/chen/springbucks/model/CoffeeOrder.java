package sizhe.chen.springbucks.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name = "T_ORDER")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class CoffeeOrder extends BaseEntity implements Serializable {

    private String customer;
    @Enumerated
    @Column(nullable = false)
    private OrderState state;

    @ManyToMany
    @JoinTable(name = "t_order_coffee")
    @OrderBy("id")
    private List<Coffee>  items;

}
