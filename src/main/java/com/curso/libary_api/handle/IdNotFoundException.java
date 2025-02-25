package com.curso.libary_api.handle;

public class IdNotFoundException extends RuntimeException
{
    public IdNotFoundException(String message) {

        super(message);
    }
}
