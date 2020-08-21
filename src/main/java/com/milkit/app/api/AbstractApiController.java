package com.milkit.app.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.milkit.app.common.response.AnonymouseResult;
import com.milkit.app.common.response.GenericResponse;
import com.milkit.app.domain.notice.NoticeAttach;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class AbstractApiController {

    public <T> ResponseEntity<GenericResponse<T>> apiResponse(final AnonymouseResult<T> templateResult) throws Exception {
    	return success(templateResult.body());
    }

    private <T> ResponseEntity<GenericResponse<T>> success(final T result) {
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<GenericResponse<T>>(new GenericResponse<T>(result), headers, HttpStatus.OK);
    }
    
    private <T> ResponseEntity<T> unauthorized(final T result) {
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<T>(result, headers, HttpStatus.UNAUTHORIZED);
    }
    
}
