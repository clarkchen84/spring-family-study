package sizhe.chen.resttemplate.demo;

import lombok.*;
import org.joda.money.Money;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: sizhe.chen
 * @Date: Create in 3:12 下午 2022/5/22
 * @Description:
 * @Modified:
 * @Version:
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class Coffee  implements Serializable {
    private Long id;
    private String name;
    private Money price;
    private Date createTime;
    private Date updateTime;
}
