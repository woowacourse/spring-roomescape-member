package roomescape.auth.sign.application;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.auth.sign.application.usecase.SignInUseCase;
import roomescape.auth.sign.application.usecase.SignUpUseCase;
import roomescape.auth.sign.ui.dto.SignInWebRequest;
import roomescape.auth.sign.ui.dto.SignUpWebRequest;
import roomescape.user.application.dto.UserPublicInfoResponse;

@Service
@RequiredArgsConstructor
public class SignFacadeImpl implements SignFacade {

    private final SignInUseCase signInUseCase;
    private final SignUpUseCase signUpUseCase;

    @Override
    public void signIn(final SignInWebRequest signInWebRequest,
                       final HttpServletResponse httpServletResponse) {
        signInUseCase.execute(
                signInWebRequest.toServiceRequest(),
                httpServletResponse);
    }

    @Override
    public UserPublicInfoResponse signUp(final SignUpWebRequest request) {
        return UserPublicInfoResponse.from(
                signUpUseCase.execute(request.toServiceRequest()));
    }

    // TODO signout
}
