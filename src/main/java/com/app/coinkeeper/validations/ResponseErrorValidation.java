package com.app.coinkeeper.validations;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.Map;

@Service
public class ResponseErrorValidation {

    public ResponseEntity<Object> mapValidationService(BindingResult result){
        if(result.hasErrors()){
            Map<String,String> errorMap = new HashMap<>();

            if(!CollectionUtils.isEmpty(result.getAllErrors())){
                for(ObjectError error : result.getAllErrors()){
                    errorMap.put(error.getCode(), error.getDefaultMessage());
                }
            }

            for (FieldError error : result.getFieldErrors()){
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
