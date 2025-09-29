package com.codeShuttle.practice.TestingApplication;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.assertj.core.api.Assertions;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
class TestingApplicationTests {

    int age;

    @BeforeEach
    void setup(){
        log.info("Running setup before every test case ............ ");
        age = 18;
    }

    @AfterEach
    void cleanUp(){
        log.info("Cleaning up resources before every test case ............ ");
    }

    @Test
    @DisplayName("Check whether age in Valid and greater than 17 and less than 100")
	void check_ifAge__isValid(){
        assertThat(age)
                .isGreaterThan(17)
                .isLessThan(100)
                .isNotNegative();
        log.info("Test Case for age validation will run ............ ");
    }

    @Test
    @DisplayName("Check whether person born after 2000")
    @Disabled
    void check_ifBirthYear__isValid(){

        log.info("Test Case for BirthYear validation will run ............ ");

        assertThat(2025-age)
                .isNotNegative()
                .isLessThanOrEqualTo(2000);

    }


    @Test
    @Disabled
    void testCaseToCheck_Exception(){
        assertThatThrownBy(this::throwArithmeticException)
                .isInstanceOf(ArithmeticException.class)
                .hasMessage("Divide by hero");

    }


    void throwArithmeticException(){
        try{
            int result = age/0;
        }catch(ArithmeticException exception){
            throw new ArithmeticException("Divide by zero");
        }
    }

}
