package roomescape.repository;

import java.util.Optional;
import roomescape.domain.Member;
import roomescape.domain.RegistrationDetails;

public interface MemberRepository {

    void save(final RegistrationDetails registrationDetails);

    Optional<Member> findByEmail(final String email);

    Optional<Member> findByEmailAndPassword(final String email, final String password);
}
