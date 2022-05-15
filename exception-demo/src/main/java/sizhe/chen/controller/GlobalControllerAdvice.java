package sizhe.chen.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.xml.bind.ValidationException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: sizhe.chen
 * @Date: Create in 9:08 下午 2022/5/15
 * @Description:
 * @Modified:
 * @Version:
 */
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> validationExceptionHandler(ValidationException exception){
        Map<String,String> map = new HashMap<>();
        map.put("message", exception.getMessage());
        return map;
    }
}
