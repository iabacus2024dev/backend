package com.iabacus.salespro.web.department.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.iabacus.salespro.web.department.response.DepartmentResponse;
import com.iabacus.salespro.web.department.response.TreeViewResponse;
import com.iabacus.salespro.web.department.service.DepartmentService;

@RequiredArgsConstructor
@RestController
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/api/v1/teams")
    public ResponseEntity<List<DepartmentResponse>> getTeams() {
        return ResponseEntity.ok(departmentService.getTeams());
    }

    @GetMapping("/api/v1/teams/tree")
    public ResponseEntity<List<TreeViewResponse>> getDepartmentTreeView() {
        return ResponseEntity.ok(departmentService.getTreeView());
    }

}
