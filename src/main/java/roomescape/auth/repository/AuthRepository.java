package roomescape.auth.repository;

import roomescape.entity.Member;

public interface AuthRepository {
    boolean isExistEmail(String email);

    Member save(Member member);

    Member findByEmail(String email);

    Member findMemberById(long id);
}
