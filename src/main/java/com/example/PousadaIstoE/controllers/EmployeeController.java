package com.example.PousadaIstoE.controllers;

import com.example.PousadaIstoE.model.Employee;
import com.example.PousadaIstoE.services.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> findAllEmployees(){
        return employeeService.findAllEmployees();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(Employee employee){
        return employeeService.addEmployee(employee);
    }
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(Long id){
        employeeService.deleteEmployee(id);
    }
}
