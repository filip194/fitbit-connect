package hr.fitbit.demo.fitbitconnect.apisupport.controller;

import hr.fitbit.demo.fitbitconnect.apisupport.apimodel.error.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler
    public ResponseEntity<Error> handleHttpClientErrorException(HttpClientErrorException e) {
        log.error(e.getResponseBodyAsString(), e);
        return new ResponseEntity<>(new Error(e.getMessage()), e.getStatusCode());
    }

    @ExceptionHandler
    public ResponseEntity<Error> handleServerErrorException(HttpServerErrorException e) {
        log.error(e.getResponseBodyAsString());
        return new ResponseEntity<>(new Error(e.getMessage()), e.getStatusCode());
    }

}

