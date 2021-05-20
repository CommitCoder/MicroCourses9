package com.kuba.courses.model;


import com.kuba.courses.exception.CourseError;
import com.kuba.courses.exception.CourseException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document
@Getter
@Setter
public class Course {

    @Id
    private String code;
    @NotBlank
    private String name;
    private String description;
    @NotNull
    @Future
    private LocalDateTime startDate;
    @NotNull
    @Future
    private LocalDateTime endDate;
    @Min(0)
    private Long participantsLimit;
    @NotNull
    @Min(0)
    private Long participantsNumber;

//    @Enumerated(EnumType.STRING) // do mongo nie jest to wymagane
    @NotNull
    private Status status;

    private List<CourseMember> courseMembers = new ArrayList<>();

    public enum Status{
        ACTIVE, INACTIVE, FULL
    }

    public void validateCourse(){
        validateCourseDate();
        validateParticipantsLimit();
        validateFullStatus();
    }

    public void incrementParticipantsNumber(){
        participantsNumber++;
        if(participantsLimit.equals(participantsNumber)){
            setStatus(Course.Status.FULL);
        }
    }

    public void validateCourseDate(){
        if(startDate.isAfter(endDate)){
            throw new CourseException(CourseError.COURSE_START_DATE_IS_AFTER_END_DATE);
        }
    }

    public void validateParticipantsLimit(){
        if(participantsNumber > participantsLimit){
            throw new CourseException(CourseError.COURSE_PARTICIPANTS_LIMIT_IS_EXCEED);
        }
    }

    public void validateFullStatus(){

        if(participantsLimit.equals(participantsNumber) && !status.equals(Status.FULL)){
            throw new CourseException(CourseError.COURSE_FULL_STATUS_ERROR);
        }

        if(Course.Status.FULL.equals(status)){
            if(!participantsLimit.equals(participantsNumber)){
                throw new CourseException(CourseError.COURSE_FULL_STATUS_ERROR);
            }
        }
    }


}
