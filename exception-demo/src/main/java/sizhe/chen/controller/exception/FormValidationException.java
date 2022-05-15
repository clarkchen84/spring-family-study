package sizhe.chen.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Author: sizhe.chen
 * @Date: Create in 8:56 下午 2022/5/15
 * @Description:
 * @Modified:
 * @Version:
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
@AllArgsConstructor
public class FormValidationException extends RuntimeException{
    private BindingResult result;
}
