package com.kuba.courses.service;


import com.kuba.courses.model.dto.Student;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

// z czym będzie się łączył ten interface
// jeśli będzie kilka instancji to ribbon będize autormatycznie przekierowywał na najmniej obciążonego STUDENT-SERVICE
//COURSE-SERVICE będzie bezpośrednio uderzać do STUDENT-SERVICE
@FeignClient(name = "STUDENT-SERVICE")
@RequestMapping("/students")// uderzamy do url "/students" bo każdy zasób w student-service zaczyna się tym url
public interface StudentServiceClient {






}
