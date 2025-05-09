package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.LoginRequest;
import roomescape.dto.UserNameResponse;
import roomescape.model.user.User;
import roomescape.model.user.UserName;
import roomescape.service.LoginService;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest,
                                      HttpServletResponse response) throws BadRequestException {
        User user = loginService.login(loginRequest.email(), loginRequest.password());
        String token = loginService.createToken(user.getUserEmail()).token();

        Cookie cookie = loginService.createCookie(token);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<UserNameResponse> checkLogin(HttpServletRequest request) throws BadRequestException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String userEmail = loginService.extractUserEmailFromCookies(cookies);
            // userEmail로 user 이름 반환
            UserName userName = loginService.getUserNameByUserEmail(userEmail);
            return ResponseEntity.ok(new UserNameResponse(userName.getName()));
        }
        throw new BadRequestException("인증 실패");
    }
}
