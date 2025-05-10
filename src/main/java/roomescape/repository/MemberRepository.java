package roomescape.repository;

import java.util.List;
import roomescape.domain.Member;

public interface MemberRepository {

    void save(Member member);

    Member findByEmail(String email);

    Member findByEmailAndPassword(String email, String password);

    Member findById(Long memberId);

    List<Member> findAll();
}
