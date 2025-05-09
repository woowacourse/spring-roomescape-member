package roomescape.repository.member;

import roomescape.entity.Member;

public interface MemberRepository {

    Member findById(Long id);

    boolean existsByEmailAndPassword(String email, String password);
}
