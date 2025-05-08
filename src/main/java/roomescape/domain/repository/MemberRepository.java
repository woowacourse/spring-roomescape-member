package roomescape.domain.repository;

import roomescape.domain.model.Member;

import java.util.List;

public interface MemberRepository {

    Member findByEmailAndPassword(String email, String password);

    Member findById(Long memberId);

    List<Member> findAll();
}
