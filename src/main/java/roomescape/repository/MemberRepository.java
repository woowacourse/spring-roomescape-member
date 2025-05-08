package roomescape.repository;

import roomescape.domain.Member;

public interface MemberRepository {

    void save(Member member);

    Member findByEmail(String email);

    Member findByEmailAndPassword(String email, String password);
}
