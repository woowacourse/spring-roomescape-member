package roomescape.member.domain;

import java.util.Optional;

public interface MemberRepository {

    Long save(Member member);

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(Long id);

    void deleteById(Long id);
}
