package roomescape.member.domain;

import java.util.List;

public interface MemberRepository {

    List<Member> findAll();

    Member findByMemberId(Long id);

    Member findByMemberName(String name);

    Member findByEmail(String email);
}
