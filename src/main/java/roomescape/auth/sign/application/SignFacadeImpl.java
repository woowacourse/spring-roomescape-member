package roomescape.auth.sign.application;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.auth.sign.application.usecase.SignInUseCase;
import roomescape.auth.sign.ui.dto.SignInWebRequest;

@Service
@RequiredArgsConstructor
public class SignFacadeImpl implements SignFacade {

    private final SignInUseCase signInUseCase;

    @Override
    public void signIn(final SignInWebRequest signInWebRequest,
                       final HttpServletResponse httpServletResponse) {
        signInUseCase.execute(
                signInWebRequest.toServiceRequest(),
                httpServletResponse);
    }

    // signout
}
