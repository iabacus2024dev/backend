package kr.co.iabacus.sales.web.partners.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import kr.co.iabacus.sales.web.partners.dto.PartnersResponse;
import kr.co.iabacus.sales.web.partners.service.PartnersService;

@Slf4j
@RestController
@RequestMapping("/api/v1/partners")
@RequiredArgsConstructor
public class PartnersController {

    private final PartnersService partnersService;

    // @GetMapping
    // public List<PartnersResponse> getPartners() {
    //     return partnersService.getPartners();
    // }

    @GetMapping("/{partnersId}")
    public PartnersResponse getPartnersDetail(@PathVariable("partnersId") Long partnersId) {
        return partnersService.getPartnersDetail(partnersId);
    }

}
