package roomescape.auth.sign.application.usecase;

import jakarta.servlet.http.HttpServletResponse;
import roomescape.auth.sign.application.dto.SignInServiceRequest;

public interface SignInUseCase {

    void execute(SignInServiceRequest request, HttpServletResponse response);
}
