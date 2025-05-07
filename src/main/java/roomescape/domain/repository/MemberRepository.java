package roomescape.domain.repository;

import roomescape.domain.model.Member;

public interface MemberRepository {

    Member findByEmailAndPassword(String email, String password);
}
