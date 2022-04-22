package sizhe.chen.reactive.mongo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;

import java.util.Date;

/**
 * @Author: sizhe.chen
 * @Date: Create in 8:06 下午 2022/4/22
 * @Description:
 * @Modified:
 * @Version:
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Coffee {
    private String id;
    private String name;
    private Money price;
    private Date createTime;
    private Date updateTime;
}
