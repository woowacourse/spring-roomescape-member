package roomescape.domain.member;

import java.util.List;

public interface MemberRepository {

    Member save(Member member);

    boolean existsByEmail(String email);

    List<Member> findAll();
}
