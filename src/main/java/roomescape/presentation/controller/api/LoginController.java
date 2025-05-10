package roomescape.presentation.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.service.AuthService;
import roomescape.presentation.dto.LoginCheckResponse;
import roomescape.presentation.dto.LoginRequest;
import roomescape.presentation.dto.TokenResponse;
import roomescape.presentation.exception.BadRequestException;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    public LoginController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void login(
            @RequestBody @Valid final LoginRequest loginRequest,
            final HttpServletResponse response
    ) {
        final TokenResponse token = authService.createToken(loginRequest);
        final Cookie cookie = new Cookie("token", token.accessToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @GetMapping("/check")
    public ResponseEntity<LoginCheckResponse> loginCheck(final HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw new BadRequestException("쿠키를 입력해 주세요.");
        }
        final String token = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .orElseThrow(() -> new BadRequestException("토큰을 입력해주세요."))
                .getValue();
        final LoginCheckResponse response = authService.checkMember(token);
        return ResponseEntity.ok().body(response);
    }
}
