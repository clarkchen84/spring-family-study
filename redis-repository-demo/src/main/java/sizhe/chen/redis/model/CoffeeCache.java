package sizhe.chen.redis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.money.Money;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

/**
 * @Author: sizhe.chen
 * @Date: Create in 11:24 上午 2022/4/17
 * @Description:
 * @Modified:
 * @Version:
 */

@RedisHash(value = "springbucks-coffee",timeToLive = 60)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoffeeCache {
    @Id
    private Long id;
    @Indexed
    private String name;
    private Money price;
}
