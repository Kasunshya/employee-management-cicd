package com.example.employeemanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.employeemanagement.dto.EmployeeRequest;
import com.example.employeemanagement.dto.EmployeeResponse;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.repository.EmployeeRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeRequest request;
    private Employee employee;

    @BeforeEach
    void setUp() {
        request = new EmployeeRequest(
                "Ada", "Lovelace", "ada@example.com", "Engineering", new BigDecimal("75000"));
        employee = new Employee(
                request.firstName(), request.lastName(), request.email(),
                request.department(), request.salary());
        employee.setId(1L);
    }

    @Test
    void createEmployeeReturnsSavedEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeResponse result = employeeService.createEmployee(request);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.email()).isEqualTo("ada@example.com");
    }

    @Test
    void getAllEmployeesReturnsEmployees() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        List<EmployeeResponse> result = employeeService.getAllEmployees();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).firstName()).isEqualTo("Ada");
    }

    @Test
    void getEmployeeByIdReturnsEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeResponse result = employeeService.getEmployeeById(1L);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.department()).isEqualTo("Engineering");
    }

    @Test
    void updateEmployeeChangesDetails() {
        EmployeeRequest update = new EmployeeRequest(
                "Grace", "Hopper", "grace@example.com", "Research", new BigDecimal("85000"));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);

        EmployeeResponse result = employeeService.updateEmployee(1L, update);

        assertThat(result.firstName()).isEqualTo("Grace");
        assertThat(result.department()).isEqualTo("Research");
        verify(employeeRepository).save(employee);
    }

    @Test
    void deleteEmployeeDeletesExistingEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).delete(employee);
    }

    @Test
    void missingEmployeeThrowsResourceNotFoundException() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.getEmployeeById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 99");
    }
}
