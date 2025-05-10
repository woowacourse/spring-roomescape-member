package roomescape.domain.repository;

import roomescape.domain.model.Member;

import java.util.List;

public interface MemberRepository {

    Long save(Member member);

    Member findByEmail(String email);

    Member findById(Long memberId);

    List<Member> findAll();

    boolean existByEmail(String email);
}
