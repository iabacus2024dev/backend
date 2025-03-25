package com.iabacus.salespro.web.role.repository;

import java.util.List;

import com.iabacus.salespro.web.role.response.RoleResponse;

public interface CustomRoleRepository {

    List<RoleResponse> findRoles();

}
