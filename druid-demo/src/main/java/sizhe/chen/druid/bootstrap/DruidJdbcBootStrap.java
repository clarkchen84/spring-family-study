package sizhe.chen.druid.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @Author: sizhe.chen
 * @Date: Create in 5:43 下午 2022/3/27
 * @Description:
 * @Modified:
 * @Version:
 */
@SpringBootApplication
@Slf4j
public class DruidJdbcBootStrap implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(DruidJdbcBootStrap.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info(dataSource.toString());
    }
}
