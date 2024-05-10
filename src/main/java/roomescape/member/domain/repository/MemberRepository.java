package roomescape.member.domain.repository;

import roomescape.exception.InvalidMemberException;
import roomescape.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);

    Optional<Member> findByEmail(String email);

    default Member getByEmail(String email) {
        return findByEmail(email).orElseThrow(() -> new InvalidMemberException("해당 이메일을 가진 회원을 찾을 수 없습니다."));
    }

    Optional<Member> findById(long id);

    default Member getById(long id) {
        return findById(id).orElseThrow(() -> new InvalidMemberException("해당 id을 가진 회원을 찾을 수 없습니다."));
    }
}
