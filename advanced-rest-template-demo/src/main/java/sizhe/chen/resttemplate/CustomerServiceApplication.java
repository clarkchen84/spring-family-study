package sizhe.chen.resttemplate;

import ch.qos.logback.core.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.tomcat.jni.Time;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sizhe.chen.resttemplate.model.Coffee;
import sizhe.chen.resttemplate.support.CustomConnectionKeepAliveStrategy;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sizhe.chen
 * @Date: Create in 4:42 下午 2022/5/22
 * @Description:
 * @Modified:
 * @Version:
 */
@SpringBootApplication
@Slf4j
public class CustomerServiceApplication implements ApplicationRunner {
    @Autowired
    private RestTemplate template;

    @Bean
    public HttpComponentsClientHttpRequestFactory factory(){
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(200);
        manager.setDefaultMaxPerRoute(20);

        CloseableHttpClient httpClient = HttpClients.custom()
                .evictIdleConnections(30, TimeUnit.SECONDS)
                .setConnectionManager(manager)
                .disableAutomaticRetries()
                .setKeepAliveStrategy(new CustomConnectionKeepAliveStrategy())
                .build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return factory;

    }
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
       return  builder.setConnectTimeout(Duration.ofMillis(100)).setReadTimeout(Duration.ofMillis(500))
                .requestFactory(this::factory).build();
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(CustomerServiceApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
            URI uri = UriComponentsBuilder
                    .fromUriString("http://localhost:8080/coffee/?name={name}")
                    .build("mocha");
            RequestEntity<Void> req = RequestEntity.get(uri)
                    .accept(MediaType.APPLICATION_XML)
                    .build();
            ResponseEntity<String> resp = template.exchange(req, String.class);
            log.info("Response Status: {}, Response Headers: {}", resp.getStatusCode(), resp.getHeaders().toString());
            log.info("Coffee: {}", resp.getBody());

            String coffeeUri = "http://localhost:8080/coffee/";
            Coffee request = Coffee.builder()
                    .name("Americano")
                    .price(Money.of(CurrencyUnit.of("CNY"), 25.00))
                    .build();
            Coffee response = template.postForObject(coffeeUri, request, Coffee.class);
            log.info("New Coffee: {}", response);

            ParameterizedTypeReference<List<Coffee>> ptr =
                    new ParameterizedTypeReference<List<Coffee>>() {};
            ResponseEntity<List<Coffee>> list = template
                    .exchange(coffeeUri, HttpMethod.GET, null, ptr);
            list.getBody().forEach(c -> log.info("Coffee: {}", c));

    }
}
