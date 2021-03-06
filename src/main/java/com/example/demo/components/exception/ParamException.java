package com.example.demo.components.exception;

/**
 * @author chen_bq
 * @description
 * @create: 2019-03-13 15:51
 **/
public class ParamException extends RuntimeException {

    private static final long serialVersionUID = -8541311111016065562L;

    public ParamException(){
        super();
    }

    public ParamException(String message) {
        super(message);
    }

    public ParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamException(Throwable cause) {
        super(cause);
    }

    protected ParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
