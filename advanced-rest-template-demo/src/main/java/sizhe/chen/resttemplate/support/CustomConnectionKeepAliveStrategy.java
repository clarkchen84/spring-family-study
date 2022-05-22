package sizhe.chen.resttemplate.support;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.util.Arrays;

/**
 * @Author: sizhe.chen
 * @Date: Create in 4:37 下午 2022/5/22
 * @Description:
 * @Modified:
 * @Version:
 */

public class CustomConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {
    private final long KEEP_ALIVE_SECONDS = 30;

    @Override
    public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
        return Arrays.asList(httpResponse.getHeaders(HTTP.CONN_KEEP_ALIVE))
               .stream().filter(h -> StringUtils.equalsIgnoreCase(h.getName(),"timeout"))
                .findFirst().map(h->NumberUtils.toLong(h.getValue(),KEEP_ALIVE_SECONDS))
                .orElse(KEEP_ALIVE_SECONDS) * 1000;

    }
}
