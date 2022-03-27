package sizhe.chen.jdbc.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author: sizhe.chen
 * @Date: Create in 10:54 下午 2022/3/26
 * @Description:
 * @Modified:
 * @Version:
 */
@SpringBootApplication
@Slf4j
public class JdbcSampleBootStrap  implements CommandLineRunner {
    @Autowired
    private DataSource dataSource;
    public static void main(String[] args) {
        SpringApplication.run(JdbcSampleBootStrap.class,args);
    }
    public void showDataSourceInfo() throws SQLException {
        log.info(dataSource.toString());
        Connection connection = dataSource.getConnection();
        log.info(connection.toString());
        connection.close();
    }

    @Override
    public void run(String... args) throws Exception {
        showDataSourceInfo();
    }
}
