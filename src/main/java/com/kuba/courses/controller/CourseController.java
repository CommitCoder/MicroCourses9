package com.kuba.courses.controller;

import com.kuba.courses.model.Course;
import com.kuba.courses.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService ) {
        this.courseService = courseService;
    }


    @GetMapping("/{code}")
    public Course getCourse(@PathVariable String code){
        return courseService.getCourse(code);
    }

    @GetMapping
    public List<Course> getCourses(@RequestParam(required = false) Course.Status status){
        return courseService.getCourses(status);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Course addCourse(@Valid @RequestBody Course course){
        course.validateCourse();
        return courseService.addCourse(course);
    }

    @DeleteMapping("/{code}")
    public void deleteCourse(@PathVariable String code){
        courseService.deleteCourse(code);
    }

    @PutMapping("/{code}")
    public Course putCourse(@PathVariable String code, @RequestBody Course course){
        course.validateCourse();
        return courseService.putCourse(code, course);
    }

    @PatchMapping("/{code}")
    public Course patchCourse(@PathVariable String code, @RequestBody Course course){
        course.validateCourse();
        return courseService.patchCourse(code, course);
    }

    @PostMapping("{courseCode}/student/{studentId}")
    public ResponseEntity<?> courseEnrollment(@PathVariable String courseCode, @PathVariable Long studentId){
        courseService.courseEnrollment(studentId, courseCode);
        return ResponseEntity.ok().build();
    }

}
