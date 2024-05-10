package roomescape.member.domain.repository;

import roomescape.exception.InvalidMemberException;
import roomescape.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);

    Optional<Member> findByEmail(String email);

    default Member getByEmail(String email) {
        return findByEmail(email).orElseThrow(() -> new InvalidMemberException());
    }

    Optional<Member> findById(long id);

    default Member getById(long id) {
        return findById(id).orElseThrow(() -> new InvalidMemberException());
    }
}
