package com.app.common;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;

public class HandleValidError {

    public List<String> getErrorList(BindingResult result) {
        
        List<String> errors = result.getFieldErrors().stream().map(
            err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage()
        ).collect(Collectors.toList());        

        return errors;
    }
    
}