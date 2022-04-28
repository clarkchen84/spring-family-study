package sizhe.chen.spring.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "t_order")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CoffeeOrder extends BaseEntity implements Serializable {

    private String customer;
    @Enumerated
    @Column(nullable = false)
    private OrderState state;

    @ManyToMany
    @JoinTable(name = "t_coffee_order")
    @OrderBy("id")
    private List<Coffee> items;

}
