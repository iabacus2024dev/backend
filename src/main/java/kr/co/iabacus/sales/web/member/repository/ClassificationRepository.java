package kr.co.iabacus.sales.web.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.iabacus.sales.web.member.domain.Classification;
import kr.co.iabacus.sales.web.member.domain.ClassificationCode;

public interface ClassificationRepository extends JpaRepository<Classification, Long> {

    List<Classification> findByCode(ClassificationCode code);

}
