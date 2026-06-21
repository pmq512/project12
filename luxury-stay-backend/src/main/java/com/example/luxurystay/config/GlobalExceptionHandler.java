package com.example.luxurystay.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        logger.error("IllegalArgumentException: ", e);
        model.addAttribute("error", e.getMessage());
        return "error";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(MethodArgumentNotValidException e, Model model) {
        logger.error("MethodArgumentNotValidException: ", e);
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        model.addAttribute("error", "Validation failed");
        model.addAttribute("errors", errors);
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGlobalException(Exception e, Model model) {
        logger.error("GlobalException: ", e);
        model.addAttribute("error", "服务器内部错误" + e.getMessage());
        return "error";
    }
}
