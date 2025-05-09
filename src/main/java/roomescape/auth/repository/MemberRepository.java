package roomescape.auth.repository;

import roomescape.auth.entity.Member;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findById(Long userId);
    Optional<Member> findByEmailAndPassword(String email, String password);
    Optional<Member> findByEmail(String email);
    Member save(Member member);
}
