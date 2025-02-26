package kr.co.iabacus.sales.web.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import kr.co.iabacus.sales.web.project.dto.ProjectCreateRequest;
import kr.co.iabacus.sales.web.project.repository.ProjectRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    // 프로젝트 등록
    @Transactional
    public void createProject(ProjectCreateRequest request) {
        projectRepository.save(request.toEntity());
    }
}
