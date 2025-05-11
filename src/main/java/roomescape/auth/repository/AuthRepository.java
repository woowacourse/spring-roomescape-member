package roomescape.auth.repository;

import roomescape.entity.Member;

public interface AuthRepository {
    boolean isExistEmail(String email);

    Member save(Member member);
}
