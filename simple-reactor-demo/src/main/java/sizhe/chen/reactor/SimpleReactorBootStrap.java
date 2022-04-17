package sizhe.chen.reactor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.TreeMap;

/**
 * @Author: sizhe.chen
 * @Date: Create in 9:51 下午 2022/4/17
 * @Description:
 * @Modified:
 * @Version:
 */
@SpringBootApplication
@Slf4j
public class SimpleReactorBootStrap implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(SimpleReactorBootStrap.class,args);
    }
    @Override
    public void run(String... args) throws Exception {
        Flux.range(1,6)
               // .publishOn(Schedulers.elastic())
                .doOnRequest(n -> log.info("Request {} number",n))
                .doOnComplete(() -> log.info("Publisher Complete 1"))
                .publishOn(Schedulers.elastic())
                .map(n -> {
                        log.info("publish {},{}",Thread.currentThread(),n);
                        //异常处理
                      // return  10/(n-3);
                        return  n;
                })
                .doOnComplete(() -> log.info("Publisher Complete 2"))
                .subscribeOn(Schedulers.single())
                .onErrorResume(e -> {
                    log.error("Exception : {} " ,e.toString());
                    return Mono.just(-1);
                })
                //.onErrorReturn(-1)
                .subscribe(i -> log.info("Subscribe {} : {}", Thread.currentThread(),i),
                        e->log.error("errors {} ", e,toString()),
                        ()-> log.info("Subscriber Complete")
                        ,s-> s.request(4)
                        );
        Thread.sleep(1000);
    }
}
