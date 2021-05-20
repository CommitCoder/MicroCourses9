package com.kuba.courses.service;

import com.kuba.courses.exception.CourseError;
import com.kuba.courses.exception.CourseException;
import com.kuba.courses.model.Course;
import com.kuba.courses.model.CourseMember;
import com.kuba.courses.model.dto.Student;
import com.kuba.courses.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final StudentServiceClient studentServiceClient;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository, StudentServiceClient studentServiceClient) {
        this.courseRepository = courseRepository;
        this.studentServiceClient = studentServiceClient;
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

    @Override
    public void courseEnrollment(Long studentId, String courseCode) {
        // first check if we can sign in into specific course, only then we make request to StudentService
        Course course = getCourse(courseCode);
        validateCourseStatus(course);
        // we are going to connect with StudentService
        Student student = studentServiceClient.getStudentById(studentId);
        validateStudentBeforeCourseEnrollment(course, student);
        course.incrementParticipantsNumber();
        course.getCourseMembers().add(new CourseMember(student.getEmail()));
        courseRepository.save(course);

    }

    private void validateStudentBeforeCourseEnrollment(Course course, Student student) {
        // validation just in case, already done in student service (returns only active students)
        System.out.println();
        if(!Student.Status.ACTIVE.equals(student.getStatus())){
            throw new CourseException(CourseError.STUDENT_IS_NOT_ACTIVE);
        }
        // check if we are going to add member with email that already exists
        if(course.getCourseMembers().stream()
                .anyMatch(member -> student.getEmail().equals(member.getEmail()))){
            throw new CourseException(CourseError.STUDENT_ALREADY_ENROLLED);
        }
    }

    private void validateCourseStatus(Course course) {
        if(!Course.Status.ACTIVE.equals(course.getStatus())){
            throw new CourseException(CourseError.COURSE_IS_NOT_ACTIVE);
        }
    }


}
