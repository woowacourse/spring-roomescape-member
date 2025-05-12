package roomescape.repository.member;

import java.util.List;
import roomescape.domain.member.Member;

public interface MemberRepository {

    Member findById(Long id);

    boolean existsByEmail(String email);

    Member findByEmail(String email);

    List<Member> findAll();
}
