package com.iabacus.salespro.web.project.domain;

public enum ProjectType {
    SI, SM;

    public static ProjectType of(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return ProjectType.valueOf(value);
    }
}
