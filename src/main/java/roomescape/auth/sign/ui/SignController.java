package roomescape.auth.sign.ui;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.auth.annotation.AuthenticatedUser;
import roomescape.auth.resolver.UserSession;
import roomescape.auth.sign.application.SignFacade;
import roomescape.auth.sign.ui.dto.SignInWebRequest;

@Controller
@RequiredArgsConstructor
public class SignController {

    private final SignFacade signFacade;

    @PostMapping("/sign-in")
    public ResponseEntity<Void> signIn(@RequestBody final SignInWebRequest signInWebRequest,
                                       final HttpServletResponse response) {
        signFacade.signIn(signInWebRequest, response);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sign-in/check")
    public ResponseEntity<UserSessionResponse> checkSignIn(@AuthenticatedUser final UserSession session) {
        return ResponseEntity.ok(
                UserSessionResponse.from(session));
    }
}
