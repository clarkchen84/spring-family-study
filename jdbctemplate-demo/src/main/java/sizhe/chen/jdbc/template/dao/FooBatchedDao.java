package sizhe.chen.jdbc.template.dao;

import com.fasterxml.jackson.databind.util.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;
import sizhe.chen.jdbc.template.bean.Foo;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: sizhe.chen
 * @Date: Create in 6:45 下午 2022/3/27
 * @Description:
 * @Modified:
 * @Version:
 */
@Repository
public class FooBatchedDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void batchInsert(){
        jdbcTemplate.batchUpdate("INSERT INTO FOO (BAR) VALUES (?)", new BatchPreparedStatementSetter(){

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, "b-" + i);
            }

            @Override
            public int getBatchSize() {
                return 2;
            }
        });

        List<Foo> fooList =new ArrayList<>();
        fooList.add(Foo.builder().bar("b-100").build());
        fooList.add(Foo.builder().bar("b-101").build());
        namedParameterJdbcTemplate.batchUpdate("INSERT INTO FOO (BAR) VALUES (:bar)", SqlParameterSourceUtils.createBatch(fooList));

    }
}
