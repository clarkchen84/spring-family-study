package sizhe.chen.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * @Author: sizhe.chen
 * @Date: Create in 2:42 下午 2022/5/15
 * @Description:
 * @Modified:
 * @Version:
 */
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = "hibernateLazyInitializer")
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Date createTime;

    @UpdateTimestamp
    private Date updateTime;
}
