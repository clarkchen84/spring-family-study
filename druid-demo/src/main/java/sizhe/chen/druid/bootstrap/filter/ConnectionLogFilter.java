package sizhe.chen.druid.bootstrap.filter;

import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.ConnectionProxy;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * @Author: sizhe.chen
 * @Date: Create in 5:44 下午 2022/3/27
 * @Description:
 * @Modified:
 * @Version:
 */
@Slf4j
public class ConnectionLogFilter extends FilterEventAdapter {
    @Override
    public void connection_connectBefore(FilterChain chain, Properties info) {
        log.info("Before Connection");
    }

    @Override
    public void connection_connectAfter(ConnectionProxy connection) {
        log.info("After Connection");
    }
}
