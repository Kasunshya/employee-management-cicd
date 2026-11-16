package com.example.employeemanagement.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.employeemanagement.exception.GlobalExceptionHandler;
import com.example.employeemanagement.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class EmployeeControllerTest {

    private final MockMvc mockMvc = MockMvcBuilders
            .standaloneSetup(new EmployeeController(new EmployeeService(null)))
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();

    @Test
    void invalidEmployeeInputReturnsBadRequest() throws Exception {
        String invalidRequest = """
                {
                  "firstName": "",
                  "lastName": "Lovelace",
                  "email": "not-an-email",
                  "department": "Engineering",
                  "salary": -1
                }
                """;

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.messages.firstName").exists())
                .andExpect(jsonPath("$.messages.email").exists())
                .andExpect(jsonPath("$.messages.salary").exists());
    }
}
