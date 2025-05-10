package roomescape.repository;

import java.util.List;
import java.util.Optional;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberEmail;
import roomescape.domain.member.MemberEncodedPassword;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberRole;

public interface MemberRepository {

    Member save(MemberEmail email, MemberName name, MemberEncodedPassword password, MemberRole role);

    Optional<Member> findByEmail(MemberEmail email);

    Optional<Member> findById(Long id);

    List<Member> findAll();
}
