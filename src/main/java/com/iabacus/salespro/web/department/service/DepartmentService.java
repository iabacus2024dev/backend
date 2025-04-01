package com.iabacus.salespro.web.department.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.web.department.domain.Department;
import com.iabacus.salespro.web.department.repository.DepartmentRepository;
import com.iabacus.salespro.web.department.response.TreeViewResponse;
import com.iabacus.salespro.web.employee.domain.Employee;
import com.iabacus.salespro.web.employee.repository.EmployeeRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public List<String> getTeams() {
        return departmentRepository.findTeams().stream().map(Department::getName).toList();
    }

    public List<String> getDepartments() {
        return departmentRepository.findDepartments().stream().map(Department::getName).toList();
    }

    public List<TreeViewResponse> getTreeView() {
        List<Department> departments = departmentRepository.findTreeViewWithEmployees();
        List<Employee> employees = employeeRepository.findEmployees();

        Map<Long, TreeViewResponse> departmentMap = buildDepartmentMap(departments);
        addEmployeesToDepartments(employees, departmentMap);
        return getRootDepartments(departments, departmentMap);
    }

    private Map<Long, TreeViewResponse> buildDepartmentMap(List<Department> departments) {
        return departments.stream()
            .collect(Collectors.toMap(Department::getId,
                department -> new TreeViewResponse(department.getId(), department.getName(), new ArrayList<>()))
            );
    }

    private void addEmployeesToDepartments(List<Employee> employees, Map<Long, TreeViewResponse> departmentMap) {
        employees.forEach(employee -> {
            departmentMap.getOrDefault(employee.getDepartmentId(), new TreeViewResponse())
                .getChildren().add(new TreeViewResponse(employee.getDepartmentId(), employee.getId(), getName(employee)));
        });
    }

    private List<TreeViewResponse> getRootDepartments(List<Department> departments, Map<Long, TreeViewResponse> departmentMap) {
        List<TreeViewResponse> rootDepartments = new ArrayList<>();
        for (Department department : departments) {
            TreeViewResponse response = departmentMap.get(department.getId());
            if (department.getParent() == null) {
                rootDepartments.add(response);
            } else {
                departmentMap.get(department.getParent().getId()).getChildren().add(response);
            }
        }

        return rootDepartments;
    }

    private String getName(Employee employee) {
        return employee.getName() + " " + employee.getRank();
    }

}
