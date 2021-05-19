package com.kuba.courses.service;

import com.kuba.courses.exception.CourseError;
import com.kuba.courses.exception.CourseException;
import com.kuba.courses.model.Course;
import com.kuba.courses.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> getCourses(Course.Status status) {
        if(status!=null){
            return courseRepository.findAllByStatus(status);
        }
        return courseRepository.findAll();
    }

    @Override
    public Course getCourse(String code) {
        return courseRepository.findById(code)
                .orElseThrow(()-> new CourseException(CourseError.COURSE_NOT_FOUND));
    }

    @Override
    public Course addCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public void deleteCourse(String code) {
        Course course = courseRepository.findById(code)
                .orElseThrow(() -> new CourseException(CourseError.COURSE_NOT_FOUND));
        course.setStatus(Course.Status.INACTIVE);
        courseRepository.delete(course);
    }

    private Course editCourseMethod(Course course, Course courseFromDb){

        courseFromDb.setName(course.getName());
        courseFromDb.setDescription(course.getDescription());

        courseFromDb.setStartDate(course.getStartDate());
        courseFromDb.setEndDate(course.getEndDate());

        courseFromDb.setParticipantsLimit(course.getParticipantsLimit());
        courseFromDb.setParticipantsNumber(course.getParticipantsNumber());

        courseFromDb.setStatus(course.getStatus());
        return courseFromDb;

    }

    @Override
    public Course putCourse(String code, Course course) {

        // TO DO @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


        return courseRepository.findById(code).map(
                courseFromDb -> {
                    return courseRepository.save(editCourseMethod(course,courseFromDb));
                }
        ).orElseThrow(() -> new CourseException(CourseError.COURSE_NOT_FOUND));


    }

    @Override
    public Course patchCourse(String code, Course course) {
        return courseRepository.findById(code).map(
                courseFromDb ->{


                    // TO DO @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

                    if(!StringUtils.isEmpty(course.getName())){
                        courseFromDb.setName(course.getName());
                    }

                    courseFromDb.setDescription(course.getDescription());

                    courseFromDb.setStartDate(course.getStartDate());
                    courseFromDb.setEndDate(course.getEndDate());

                    courseFromDb.setParticipantsLimit(course.getParticipantsLimit());
                    courseFromDb.setParticipantsNumber(course.getParticipantsNumber());

                    courseFromDb.setStatus(course.getStatus());



                    return courseRepository.save(courseFromDb);
                }
        ).orElseThrow(()->new CourseException(CourseError.COURSE_NOT_FOUND));
    }


}
