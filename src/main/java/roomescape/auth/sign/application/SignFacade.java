package roomescape.auth.sign.application;

import jakarta.servlet.http.HttpServletResponse;
import roomescape.auth.sign.ui.dto.SignInWebRequest;
import roomescape.auth.sign.ui.dto.SignUpWebRequest;
import roomescape.user.application.dto.UserPublicInfoResponse;

public interface SignFacade {

    void signIn(SignInWebRequest request, HttpServletResponse response);

    UserPublicInfoResponse signUp(SignUpWebRequest request);
}
