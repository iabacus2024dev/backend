package com.iabacus.salespro.web.role.repository;

import com.iabacus.salespro.web.role.response.SettingResponse;
import com.iabacus.salespro.web.role.response.RoleResponse;

import java.util.List;

public interface CustomRoleRepository {

    List<RoleResponse> findRoles();

    List<SettingResponse> getSettingsByRole(String roleName);
}
