package com.codeShuttle.practice.TestingApplication.controllers;


import com.codeShuttle.practice.TestingApplication.TestContainerConfiguration;
import com.codeShuttle.practice.TestingApplication.dto.EmployeeDto;
import com.codeShuttle.practice.TestingApplication.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient(timeout = "100000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainerConfiguration.class)
public class AbstractIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    Employee testEmployee = Employee.builder()
            .email("anuj@gmail.com")
            .name("Anuj")
            .salary(200L)
            .build();

    EmployeeDto testEmployeeDto = EmployeeDto.builder()
            .email("anuj@gmail.com")
            .name("Anuj")
            .salary(200L)
            .build();

}
