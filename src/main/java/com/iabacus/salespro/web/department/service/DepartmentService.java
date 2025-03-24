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

    public List<TreeViewResponse> getTreeView() {
        List<Department> departments = departmentRepository.findTreeViewWithEmployees();
        List<Employee> employees = employeeRepository.findAll();

        Map<Long, TreeViewResponse> departmentMap = departments.stream()
            .collect(Collectors.toMap(
                Department::getId,
                department -> TreeViewResponse.builder()
                    .id(department.getId())
                    .name(department.getName())
                    .children(new ArrayList<>())
                    .build()
            ));

        for (Employee employee : employees) {
            Long deptId = employee.getDepartmentId();
            if (departmentMap.containsKey(deptId)) {
                departmentMap.get(deptId).getChildren()
                    .add(TreeViewResponse.builder()
                        .id(employee.getId())
                        .name(employee.getName())
                        .build());
            }
        }

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

}
