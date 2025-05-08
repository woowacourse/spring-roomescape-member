package roomescape.repository;

import roomescape.domain.RegistrationDetails;

public interface MemberRepository {

    void save(final RegistrationDetails registrationDetails);
}
