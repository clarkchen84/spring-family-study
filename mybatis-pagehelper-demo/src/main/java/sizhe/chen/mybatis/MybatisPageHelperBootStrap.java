package sizhe.chen.mybatis;

import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import sizhe.chen.mybatis.mapper.CoffeeMapper;
import sizhe.chen.mybatis.model.Coffee;

import java.util.List;

/**
 * @Author: sizhe.chen
 * @Date: Create in 9:30 下午 2022/4/10
 * @Description:
 * @Modified:
 * @Version:
 */
@SpringBootApplication
@EnableTransactionManagement
@Slf4j
@MapperScan("sizhe.chen.mybatis.mapper")
public class MybatisPageHelperBootStrap implements ApplicationRunner {

    @Autowired
    private CoffeeMapper coffeeMapper;

    public static void main(String[] args) {
        SpringApplication.run(MybatisPageHelperBootStrap.class,args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        coffeeMapper.findAllWithRowBounds(new RowBounds(1,3))
                .forEach(c -> log.info("page(1) coffee{}",c));
        coffeeMapper.findAllWithRowBounds(new RowBounds(2,3))
                .forEach(c -> log.info("page(2) coffee{}",c));
        log.info("===");
        coffeeMapper.findAllWithRowBounds(new RowBounds(1, 0))
                .forEach(c -> log.info("Page(1) Coffee {}", c));
        log.info("===");
        coffeeMapper.findAllWithParams(1, 3)
                .forEach(c -> log.info("Page(1) Coffee {}", c));
        List<Coffee> list = coffeeMapper.findAllWithParams(2, 3);
        PageInfo page = new PageInfo(list);
        log.info("PageInfo: {}", page);
    }

}
