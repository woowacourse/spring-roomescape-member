package roomescape.repository;

import java.util.Optional;
import roomescape.domain.LoginMember;
import roomescape.domain.RegistrationDetails;

public interface MemberRepository {

    void save(final RegistrationDetails registrationDetails);

    Optional<LoginMember> findByEmail(final String email);

    Optional<LoginMember> findByEmailAndPassword(final String email, final String password);
}
