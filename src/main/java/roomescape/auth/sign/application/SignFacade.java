package roomescape.auth.sign.application;

import jakarta.servlet.http.HttpServletResponse;
import roomescape.auth.sign.ui.dto.SignInWebRequest;

public interface SignFacade {

    void signIn(SignInWebRequest signInWebRequest, HttpServletResponse response);

}
