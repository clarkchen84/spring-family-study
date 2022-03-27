package sizhe.chen.multidatasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @Author: sizhe.chen
 * @Date: Create in 8:20 上午 2022/3/27
 * @Description:
 * @Modified:
 * @Version:
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class, JdbcTemplateAutoConfiguration.class})
@Slf4j
public class MultiDatasourceBootStrap implements CommandLineRunner{
    public static void main(String[] args) {
        SpringApplication.run(MultiDatasourceBootStrap.class,args);
    }

    @Autowired
    @Qualifier("fooDataSource")
    private DataSource foo;
    @Autowired
    @Qualifier("barDataSource")
    private DataSource bar;

    @Bean
    @ConfigurationProperties("foo.datasource")
    public DataSourceProperties fooDataSourceProperties(){
        return  new DataSourceProperties();
    }
    @Bean
    @ConfigurationProperties("bar.datasource")
    public DataSourceProperties barDataSourceProperties(){
        return  new DataSourceProperties();
    }

    @Bean
    public DataSource fooDataSource(@Qualifier("fooDataSourceProperties") DataSourceProperties fooDataSourceProperties){

        log.info("foo datasource: {}", fooDataSourceProperties.getUrl());
        return fooDataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    public DataSource barDataSource(@Qualifier("barDataSourceProperties") DataSourceProperties barDataSourceProperties){

        log.info("foo datasource: {}", barDataSourceProperties.getUrl());
        return barDataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    public PlatformTransactionManager barTxManager(@Qualifier("barDataSource") DataSource barDataSource) {
        return new DataSourceTransactionManager(barDataSource);
    }

    @Bean
    public PlatformTransactionManager fooTxManager(@Qualifier("fooDataSource")DataSource fooDataSource) {
        return new DataSourceTransactionManager(fooDataSource);
    }

    @Override
    public void run(String... args) throws Exception {

        Connection conn = foo.getConnection();
        log.info(conn.toString());
        conn.close();

        conn = bar.getConnection();
        log.info(conn.toString());
        conn.close();

    }

}
