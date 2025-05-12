package roomescape.persistence;

import java.util.List;
import java.util.Optional;
import roomescape.business.Member;

public interface MemberRepository {

    List<Member> findAll();

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

    Long add(Member member);
}
