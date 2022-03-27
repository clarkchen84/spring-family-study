package sizhe.chen.jdbc.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import sizhe.chen.jdbc.template.dao.FooBatchedDao;
import sizhe.chen.jdbc.template.dao.FooDao;

import javax.sql.DataSource;

/**
 * @Author: sizhe.chen
 * @Date: Create in 6:40 下午 2022/3/27
 * @Description:
 * @Modified:
 * @Version:
 */
@SpringBootApplication
@Slf4j
public class JdbcTemplateBootStrap implements CommandLineRunner {

    @Autowired
    private FooDao fooDao;
    @Autowired
    private FooBatchedDao fooBatchedDao;

    public static void main(String[] args) {
        SpringApplication.run(JdbcTemplateBootStrap.class,args);
    }

    @Bean
    @Autowired
    public SimpleJdbcInsert simpleJdbcInsert(JdbcTemplate jdbcTemplate){
        return new SimpleJdbcInsert(jdbcTemplate).withTableName("FOO").usingGeneratedKeyColumns("ID");
    }
    @Bean
    @Autowired
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource){
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public void run(String... args) throws Exception {
        fooDao.insertData();
        fooBatchedDao.batchInsert();
        fooDao.listData();
    }
}
