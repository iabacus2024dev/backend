package com.iabacus.salespro.web.department.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DepartmentController {

    //@PreAuthorize("hasAuthority('트리뷰 조회')")
    @GetMapping("/api/v1/teams/tree-view")
    public void getDepartmentTreeView() {
    }

}
