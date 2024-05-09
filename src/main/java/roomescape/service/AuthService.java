package roomescape.service;

import roomescape.service.request.AuthenticationRequest;

public interface AuthService {

    boolean verify(AuthenticationRequest request);
}
