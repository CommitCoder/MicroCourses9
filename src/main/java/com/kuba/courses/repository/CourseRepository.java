package com.kuba.courses.repository;


import com.kuba.courses.model.Course;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


// nie musimy dawać adnotacji @Repository bo dziedziczymy po MongoRepository które jest już Repository
//@Repository
public interface CourseRepository extends MongoRepository<Course, String> {


    List<Course> findAllByStatus(Course.Status status);

}
