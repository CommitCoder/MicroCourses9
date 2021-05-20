package com.kuba.courses.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CourseExceptionHandler {

    @ExceptionHandler(value = CourseException.class)
    public ResponseEntity<ErrorInfo> handleException(CourseException courseException){
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        if(CourseError.COURSE_NOT_FOUND.equals(courseException.getCourseError())){
            httpStatus = HttpStatus.NOT_FOUND;
        } else if(CourseError.COURSE_START_DATE_IS_AFTER_END_DATE.equals(courseException.getCourseError())
            || CourseError.COURSE_IS_NOT_ACTIVE.equals(courseException.getCourseError())
            || CourseError.STUDENT_IS_NOT_ACTIVE.equals(courseException.getCourseError())
            || CourseError.STUDENT_CANNOT_BE_ENROLLED.equals(courseException.getCourseError())){
            httpStatus = HttpStatus.BAD_REQUEST;
        } else if(CourseError.COURSE_PARTICIPANTS_LIMIT_IS_EXCEED.equals(courseException.getCourseError())
        || CourseError.COURSE_FULL_STATUS_ERROR.equals(courseException.getCourseError())
        || CourseError.STUDENT_ALREADY_ENROLLED.equals(courseException.getCourseError())){
            httpStatus = HttpStatus.CONFLICT;
        }
        return ResponseEntity.status(httpStatus).body(new ErrorInfo(courseException.getCourseError().getMessage()));
    }

}
