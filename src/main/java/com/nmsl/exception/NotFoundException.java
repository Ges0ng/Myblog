package com.nmsl.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Author Paracosm
 * @Date 2020/12/30 20:29
 * @Version 1.0
 */

/*要继承runtimeexception才能捕获
* 如果要跳转到404页面还需要指定一个返回的状态码 @ResponseStatus(HttpStatus.NOT_FOUND)
* */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
