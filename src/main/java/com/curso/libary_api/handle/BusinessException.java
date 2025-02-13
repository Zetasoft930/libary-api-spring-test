package com.curso.libary_api.handle;

public class BusinessException extends RuntimeException
{
    public BusinessException(String message) {

        super(message);
    }
}
