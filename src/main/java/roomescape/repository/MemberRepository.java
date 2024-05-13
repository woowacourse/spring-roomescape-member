package roomescape.repository;

import java.util.List;
import roomescape.domain.Member;

public interface MemberRepository {

    List<Member> findAll();

    Member findByMemberId(Long id);

    Member findByEmail(String email);
}
