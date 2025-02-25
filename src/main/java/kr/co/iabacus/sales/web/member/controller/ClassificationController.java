package kr.co.iabacus.sales.web.member.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import kr.co.iabacus.sales.web.member.domain.ClassificationCode;
import kr.co.iabacus.sales.web.member.service.ClassificationService;

@Slf4j
@RequestMapping("/api/v1/classifications")
@RestController
@RequiredArgsConstructor
public class ClassificationController {

    private final ClassificationService classificationService;

    @GetMapping("/{code}")
    public List<String> getClassificationRank(@PathVariable String code) {
        ClassificationCode classificationCode = ClassificationCode.valueOf(code.toUpperCase());
        return classificationService.getClassificationsByCode(classificationCode);
    }

}
