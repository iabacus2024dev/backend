package kr.co.iabacus.sales.web.member.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.co.iabacus.sales.web.member.domain.Member;
import kr.co.iabacus.sales.web.member.dto.MemberSearchCondition;

public interface MemberRepositoryCustom {

    Page<Member> searchMembers(Pageable pageable, MemberSearchCondition condition);

}

