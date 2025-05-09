package roomescape.member.repository;


import java.util.List;
import java.util.Optional;
import roomescape.member.domain.Member;

public interface MemberRepository {

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(final String email);

    void save(Member member);

    List<Member> findAll();
}
