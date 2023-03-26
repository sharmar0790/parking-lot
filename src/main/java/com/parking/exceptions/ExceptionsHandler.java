package com.parking.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatusCode status,
                                                                         WebRequest request) {
        Map<String, Object> resObject = new HashMap<>();
        int statusCodeValue = ex.getStatusCode().value();
        String supportedMethod = !ObjectUtils.isEmpty(ex.getSupportedMethods()) ? ex.getSupportedMethods()[0] : null;
        resObject.put("statusCode", statusCodeValue);
        resObject.put("supportedMethod", supportedMethod);
        resObject.put("usedMethod", ex.getMethod());
        resObject.put("message", ex.getBody().getDetail());
        return ResponseEntity.of(Optional.of(resObject));
    }

    @ExceptionHandler(value = RuntimeException.class)
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, WebRequest request) {
        Map<String, Object> resObject = new HashMap<>();
        resObject.put("message", ex.getMessage());
        resObject.put("l_message", ex.getLocalizedMessage());
        return ResponseEntity.of(Optional.of(resObject));
    }
}
