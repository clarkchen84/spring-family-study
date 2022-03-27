package sizhe.chen.jdbc.template.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import sizhe.chen.jdbc.template.bean.Foo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: sizhe.chen
 * @Date: Create in 6:09 下午 2022/3/27
 * @Description:
 * @Modified:
 * @Version:
 */
@Repository
@Slf4j
public class FooDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SimpleJdbcInsert simpleJdbcInsert;

    public void insertData(){
        Arrays.asList("a","b").forEach(
                bar -> jdbcTemplate.update("INSERT INTO FOO(BAR) VALUES(?)", bar)
        );
        HashMap<String,String> map = new HashMap<>();
        map.put("bar", "d");
        Number id = simpleJdbcInsert.executeAndReturnKey(map);
        log.info("id of d :{}" ,id.longValue());
    }

    public void listData(){
        log.info("Count: {} ", jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM FOO "
                ,Long.class));

        List<String> list = jdbcTemplate.queryForList("SELECT BAR FROM FOO", String.class);
        list.forEach(s -> log.info("Bar:{}", s));
        List<Foo> fooList = jdbcTemplate.query("SELECT * FROM FOO ", new RowMapper<Foo>() {
            @Override
            public Foo mapRow(ResultSet rs, int rowNum) throws SQLException {
                return Foo.builder().id(rs.getLong(1)).bar(rs.getString(2)).build();
            }
        });

        list.forEach(foo -> log.info("foo :{}" , foo));
    }

}
