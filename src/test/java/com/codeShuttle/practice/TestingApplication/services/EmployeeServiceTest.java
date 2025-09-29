package com.codeShuttle.practice.TestingApplication.services;

import com.codeShuttle.practice.TestingApplication.TestContainerConfiguration;
import com.codeShuttle.practice.TestingApplication.dto.EmployeeDto;
import com.codeShuttle.practice.TestingApplication.entities.Employee;
import com.codeShuttle.practice.TestingApplication.exceptions.ResourceNotFoundException;
import com.codeShuttle.practice.TestingApplication.repositories.EmployeeRepository;
import com.codeShuttle.practice.TestingApplication.services.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestContainerConfiguration.class)
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee mockEmployee;
    private EmployeeDto mockEmployeeDto;


    @BeforeEach
    void setupTest_Data(){
        mockEmployee = Employee.builder()
                .name("archit")
                .email("archit@google.com")
                .salary(100L)
                .id(1L)
                .build();

        mockEmployeeDto = modelMapper.map(mockEmployee, EmployeeDto.class);
    }


    @Test
    void testGetEmployeeById_WhenEmployeeIdIsPresent_ThenReturnEmployeeDto(){
        // assign
        long id = mockEmployee.getId();

        //when -> Defining when method called on a mock object what method should return
        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockEmployee)); //stubbing


        EmployeeDto employeeDto = employeeService.getEmployeeById(id);

        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployeeDto.getEmail());
        assertThat(employeeDto.getId()).isEqualTo(id);
        //verify

        verify(employeeRepository, only()).findById(id);
        //mockObject,only() -> only findById method is called
    }

    @Test
    void testGetEmployeeById_WhenEmployeeIdIsNotPresent_ThenThrowResourceNotFoundException() {
        // assign
        long id = 2;//mockEmployee.getId();

        //when --> this repo will return in case record is missing empty optional
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(()->{
            employeeService.getEmployeeById(1L);
        }).isInstanceOf(ResourceNotFoundException.class).hasMessage("Employee not found with id: "+1L);

        verify(employeeRepository,only()).findById(anyLong());

    }

    @Test
    void testCreateNewEmployee_WhenValidEmployee_ThenCreateNewEmployee(){

        //        assign
        when(employeeRepository.findByEmail(anyString())).thenReturn(List.of());
        when(employeeRepository.save(any(Employee.class))).thenReturn(mockEmployee);
//        act

        EmployeeDto employeeDto = employeeService.createNewEmployee(mockEmployeeDto);
//        assert
        assertThat(employeeDto).isNotNull();
        assertThat(employeeDto.getEmail()).isEqualTo(mockEmployeeDto.getEmail());

        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(employeeArgumentCaptor.capture());

        Employee capturedEmployee = employeeArgumentCaptor.getValue();
        assertThat(capturedEmployee.getEmail()).isEqualTo(mockEmployee.getEmail());

    }

    @Test
    void testCreateNewEmployee_whenAttemptingToCreateEmployeeWithExistingEmail_thenThrowException(){
        when(employeeRepository.findByEmail(mockEmployeeDto.getEmail())).thenReturn(List.of(mockEmployee));

        assertThatThrownBy(()->{employeeService.createNewEmployee(mockEmployeeDto);})
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee already exists with email: "+mockEmployee.getEmail());

        verify(employeeRepository).findByEmail(mockEmployeeDto.getEmail());
        verify(employeeRepository, never()).save(any());

    }

    @Test
    void testUpdateEmployee_whenValidEmployee_thenUpdateEmployee(){

        //when
        when(employeeRepository.findById(mockEmployee.getId())).thenReturn(Optional.of(mockEmployee));

        mockEmployeeDto.setName("Random name");
        mockEmployeeDto.setSalary(199L);

        Employee newEmployee = modelMapper.map(mockEmployeeDto, Employee.class);

        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);


//        act
        EmployeeDto updatedEmployeeDto = employeeService.updateEmployee(mockEmployeeDto.getId(), mockEmployeeDto);

        assertThat(updatedEmployeeDto).isEqualTo(mockEmployeeDto);

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(any());


    }

    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExists_thenThrowException(){

        //when
        when(employeeRepository
                .findById(mockEmployee.getId()))
                .thenReturn(Optional.empty());


        //act

        assertThatThrownBy(()->{
            employeeService.updateEmployee(mockEmployeeDto.getId(),mockEmployeeDto);
        }).isInstanceOf(ResourceNotFoundException.class).hasMessage("Employee not found with id: "+mockEmployeeDto.getId());

        verify(employeeRepository,atMostOnce()).findById(mockEmployeeDto.getId());
        verify(employeeRepository,never()).save(mockEmployee);
    }

    @Test
    void testUpdateEmployee_whenAttemptingToUpdateEmail_thenThrowException(){

        //assign
        long id = 1;
        when(employeeRepository.findById(id))
                .thenReturn(Optional.of(mockEmployee));

        mockEmployeeDto.setName("Random");
        mockEmployeeDto.setEmail("random@gmail.com");

        assertThatThrownBy(() -> employeeService.updateEmployee(mockEmployeeDto.getId(), mockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The email of the employee cannot be updated");

        verify(employeeRepository).findById(mockEmployeeDto.getId());
        verify(employeeRepository, never()).save(any());


    }


    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExists_thenThrowException() {
        //create
        when(employeeRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(()->{
            employeeService.deleteEmployee(1L);
        }).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: " + 1L);

        verify(employeeRepository, never()).deleteById(anyLong());

    }

    @Test
    void testDeleteEmployee_whenEmployeeIsValid_thenDeleteEmployee() {

        //create
        when(employeeRepository.existsById(1L)).thenReturn(true);

       assertThatCode(()->{
           employeeService.deleteEmployee(1L);
       }).doesNotThrowAnyException();

        verify(employeeRepository).deleteById(1L);

    }

}