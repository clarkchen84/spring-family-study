package sizhe.chen;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;


@SpringBootApplication
@Slf4j
public class ProgrammaticTransactionBootStrap  implements CommandLineRunner {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TransactionTemplate transactionTemplate;

    public static void main(String[] args) {
        SpringApplication.run(ProgrammaticTransactionBootStrap.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("数量： {}", getCount());
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                jdbcTemplate.execute("insert into foo(bar) values ('aaa')");
            }
        });
        log.info("数量： {}", getCount());
    }

    private int getCount(){
        return jdbcTemplate.queryForObject("select count(*)" +
                " from foo", Integer.class);
    }
}
