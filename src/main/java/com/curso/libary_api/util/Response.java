package com.curso.libary_api.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Response {


    private List<String> errors;


    public Response(){
        errors = new ArrayList<>();
    }



}
