package roomescape.auth.sign.application.usecase;

import jakarta.servlet.http.HttpServletResponse;
import roomescape.auth.sign.application.dto.SignInRequest;

public interface SignInUseCase {

    void execute(SignInRequest request, HttpServletResponse response);
}
