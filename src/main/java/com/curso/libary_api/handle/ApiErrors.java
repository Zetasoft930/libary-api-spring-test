package com.curso.libary_api.handle;

import java.util.Collections;
import java.util.List;

public class ApiErrors {
    private List<String> errors;

    public ApiErrors(String message) {
        this.errors = Collections.singletonList(message);
    }

    public List<String> getErrors() {
        return errors;
    }
}
