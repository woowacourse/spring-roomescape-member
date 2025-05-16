package roomescape.auth.repository;

import roomescape.auth.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    List<Member> findAll();
    Optional<Member> findById(Long userId);
    Optional<Member> findByEmail(String email);
    Member save(Member member);
}
