package com.iabacus.salespro.web.role.response;

import com.iabacus.salespro.web.role.domain.Page;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class SettingResponse {
    private Page page;
    private String actionName;
    private String rangeName;

    @QueryProjection
    public SettingResponse(Page page, String actionName, String rangeName) {
        this.page = page;
        this.actionName = actionName;
        this.rangeName = rangeName;
    }
}
