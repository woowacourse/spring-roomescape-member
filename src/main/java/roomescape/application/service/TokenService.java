package roomescape.application.service;

import roomescape.domain.model.Member;

public interface TokenService {

    String createToken(final Member member);

    String checkByToken(String token);
}
