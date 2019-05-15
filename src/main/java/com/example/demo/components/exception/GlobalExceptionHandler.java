package com.example.demo.components.exception;

import com.example.demo.model.entity.common.ObjectDataResponse;
import com.example.demo.model.entity.common.RestStatus;
import com.example.demo.model.entity.common.StatusCode;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chen_bq
 * @description
 * @create: 2019-01-15 14:55
 **/
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final ImmutableMap<Class<? extends Throwable>, RestStatus> EXCEPTION_MAPPINGS;

    static {
        final ImmutableMap.Builder<Class<? extends Throwable>, RestStatus> builder = ImmutableMap.builder();
        // SpringMVC中参数类型转换异常，常见于String找不到对应的ENUM而抛出的异常
        //参数检验非法
        builder.put(MethodArgumentTypeMismatchException.class, StatusCode.INVALID_PARAMS_CONVERSION);
        //字段校验非法，区别于参数校验
        builder.put(MethodArgumentNotValidException.class, StatusCode.INVALID_MODEL_FIELDS);
        builder.put(UnsatisfiedServletRequestParameterException.class, StatusCode.INVALID_PARAMS_CONVERSION);
        // HTTP Request Method不存在
        builder.put(HttpRequestMethodNotSupportedException.class, StatusCode.REQUEST_METHOD_NOT_SUPPORTED);
        // 要求有RequestBody的地方却传入了NULL
        builder.put(HttpMessageNotReadableException.class, StatusCode.HTTP_MESSAGE_NOT_READABLE);
        // 通常是操作过快导致DuplicateKey
        builder.put(DuplicateKeyException.class, StatusCode.DUPLICATE_KEY);

        //测试自定义异常处理
        builder.put(ParamException.class, StatusCode.INVALID_MODEL_FIELDS);

        // 其他未被发现的异常
        builder.put(Exception.class, StatusCode.SERVER_UNKNOWN_ERROR);
        EXCEPTION_MAPPINGS = builder.build();
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ObjectDataResponse exception(Exception e, HttpServletRequest request) {
        final RestStatus status = EXCEPTION_MAPPINGS.get(e.getClass());
        if(status == null){
            return ObjectDataResponse.builder()
                    .msg(e.getMessage())
                    .code(StatusCode.SERVER_UNKNOWN_ERROR.code())
                    .build();
        }
        //检索异常错误码对象
        ObjectDataResponse objectDataResponse = ObjectDataResponse.builder()
                .code(status.code())
                .msg(status.message())
                .build();
        return objectDataResponse;
    }

}
