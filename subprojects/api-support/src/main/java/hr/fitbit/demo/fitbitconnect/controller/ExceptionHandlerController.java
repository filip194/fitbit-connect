package hr.fitbit.demo.fitbitconnect.controller;

import hr.fitbit.demo.fitbitconnect.apimodel.error.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

public class ExceptionHandlerController {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler
    public ResponseEntity<Error> handleHttpClientErrorException(HttpClientErrorException e) {
        LOG.error(e.getResponseBodyAsString(), e);
        return new ResponseEntity<>(new Error(e.getMessage()), e.getStatusCode());
    }

    @ExceptionHandler
    public ResponseEntity<Error> handleServerErrorException(HttpServerErrorException e) {
        LOG.error(e.getResponseBodyAsString());
        return new ResponseEntity<>(new Error(e.getMessage()), e.getStatusCode());
    }

}

