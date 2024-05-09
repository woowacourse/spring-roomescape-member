package roomescape.service;

import roomescape.service.request.AuthenticationRequest;
import roomescape.service.response.AuthenticatedMemberInfo;

public interface AuthService {

    String authenticate(AuthenticationRequest request);

    AuthenticatedMemberInfo authorize(String token);
}
