package com.iabacus.salespro.web.department.controller;

import com.iabacus.salespro.web.department.response.DepartmentResponse;
import com.iabacus.salespro.web.department.response.TreeViewResponse;
import com.iabacus.salespro.web.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/api/v1/teams")
    public ResponseEntity<List<DepartmentResponse>> getTeams() {
        return ResponseEntity.ok(departmentService.getTeams());
    }

    @GetMapping("/api/v1/departments")
    public ResponseEntity<List<DepartmentResponse>> getDepartments() {
        return ResponseEntity.ok(departmentService.getDepartments());
    }

    @GetMapping("/api/v1/teams/tree")
    public ResponseEntity<List<TreeViewResponse>> getDepartmentTreeView() {
        return ResponseEntity.ok(departmentService.getTreeView());
    }

    @GetMapping("/api/v1/teams/tree/member")
    public ResponseEntity<List<TreeViewResponse>> getDepartmentTreeViewWithMember() {
        return ResponseEntity.ok(departmentService.getTreeViewWithMember());
    }
}
