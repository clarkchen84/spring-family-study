package sizhe.chen.springbucks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UriComponentsBuilder;
import sizhe.chen.springbucks.model.Coffee;

import java.math.BigDecimal;
import java.net.URI;

@SpringBootApplication
@Slf4j
public class CustomerServiceApplication implements ApplicationRunner, WebMvcConfigurer {

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(CustomerServiceApplication.class)
                .bannerMode(Banner.Mode.OFF)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return  builder.build();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8080/coffee/{id}")
                .build(1);
        ResponseEntity<Coffee> c = restTemplate.getForEntity(uri,Coffee.class);
        log.info("Response Status {}, Response Header {}",c.getStatusCode(),c.getHeaders().toString());

        uri = UriComponentsBuilder.fromUriString("http://localhost:8080/coffee/").build().toUri();

        Coffee coffee = Coffee.builder().name("Americano").price(new BigDecimal("25")).build();

        Coffee response = restTemplate.postForObject(uri,coffee,Coffee.class);
        log.info("new Coffee {}" , coffee);

        String str = restTemplate.getForObject(uri,String.class);

        log.info("String : {}" , str);
    }
}
