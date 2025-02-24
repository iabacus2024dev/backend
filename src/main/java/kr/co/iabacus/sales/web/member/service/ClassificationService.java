package kr.co.iabacus.sales.web.member.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import kr.co.iabacus.sales.web.member.domain.Classification;
import kr.co.iabacus.sales.web.member.domain.ClassificationCode;
import kr.co.iabacus.sales.web.member.repository.ClassificationRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClassificationService {

    private final ClassificationRepository classificationRepository;

    public List<String> getClassificationsByCode(ClassificationCode code) {
        return classificationRepository.findByCode(code)
            .stream()
            .map(Classification::getName)
            .collect(Collectors.toList());
    }

}
