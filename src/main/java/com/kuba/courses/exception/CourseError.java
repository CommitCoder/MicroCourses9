package com.kuba.courses.exception;

public enum CourseError {

    COURSE_NOT_FOUND("Course does not exists"),
    COURSE_START_DATE_IS_AFTER_END_DATE("Course start date is after end date"),
    COURSE_PARTICIPANTS_LIMIT_IS_EXCEED("Course participants limit is exceeded"),
    COURSE_FULL_STATUS_ERROR("Course full status error");

    private String message;

    CourseError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
