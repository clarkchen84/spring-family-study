package sizhe.chen.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: sizhe.chen
 * @Date: Create in 10:18 下午 2022/5/15
 * @Description:
 * @Modified:
 * @Version:
 */

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
// 增加了jackson-datatype-hibernate5就不需要这个Ignore了
//@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Date createTime;
    @UpdateTimestamp
    @Column(updatable = false)
    private Date updateTime;
}
