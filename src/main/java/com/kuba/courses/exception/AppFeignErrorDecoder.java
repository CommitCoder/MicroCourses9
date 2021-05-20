package com.kuba.courses.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AppFeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        // 4 na początku - błąd klienta
        if(HttpStatus.valueOf(response.status()).is4xxClientError()){
            throw new CourseException(CourseError.STUDENT_CANNOT_BE_ENROLLED);
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
