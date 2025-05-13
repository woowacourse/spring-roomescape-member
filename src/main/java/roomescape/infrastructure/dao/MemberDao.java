package roomescape.infrastructure.dao;

import roomescape.domain.model.Member;

import java.util.List;

public interface MemberDao {

    Long save(Member member);

    boolean existByEmail(final String email);

    Member findByEmail(String email);

    Member findById(final Long memberId);

    List<Member> findAll();
}
