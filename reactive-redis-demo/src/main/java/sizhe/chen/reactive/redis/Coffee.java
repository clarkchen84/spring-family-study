package sizhe.chen.reactive.redis;

import lombok.*;

import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Coffee {
    private Long id;
    private String name;
    private Long price;
    private Date createTime;
    private Date updateTime;
}
