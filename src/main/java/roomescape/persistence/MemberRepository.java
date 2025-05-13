package roomescape.persistence;

import java.util.List;
import java.util.Optional;
import roomescape.business.domain.member.Member;
import roomescape.business.domain.member.MemberCredential;
import roomescape.business.domain.member.SignUpMember;

public interface MemberRepository {

    Long save(SignUpMember signUpMember);

    Optional<Member> findById(Long id);

    Optional<MemberCredential> findCredentialByEmail(String email);

    List<Member> findAll();

    boolean existsByEmail(String email);
}
