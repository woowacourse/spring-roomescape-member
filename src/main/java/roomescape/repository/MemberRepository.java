package roomescape.repository;

import roomescape.domain.Member;

public interface MemberRepository {

    Member findByEmail(String email);

    Member findByEmailAndPassword(String email, String password);
}
