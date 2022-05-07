package sizhe.chen.spring.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "t_order")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CoffeeOrder extends  BaseEntity implements Serializable {
    private String customer;

    @Enumerated
    @Column(nullable = false)
    private OrderState state;

    @ManyToMany
    @JoinTable(name = "t_coffee_order")
    @OrderBy("id")
    private List<Coffee> items;
}
