package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.controller.request.LoginUserRequest;
import roomescape.controller.response.CheckLoginUserResponse;
import roomescape.controller.response.LoginUserResponse;
import roomescape.service.UserService;
import roomescape.service.param.LoginUserParam;
import roomescape.service.result.LoginUserResult;

@Controller
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(final UserService userService, final JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(@RequestBody LoginUserRequest loginUserRequest, HttpServletResponse response) {
        LoginUserParam loginUserParam = loginUserRequest.toServiceParam();
        LoginUserResult user = userService.login(loginUserParam);

        String token = jwtTokenProvider.createToken(user);
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().body(LoginUserResponse.from(user));
    }

    @GetMapping("/login/check")
    public ResponseEntity<CheckLoginUserResponse> loginCheck(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        String token = jwtTokenProvider.extractTokenFromCookie(cookies);
        Long id = jwtTokenProvider.extractIdFromToken(token);

        return ResponseEntity.ok().body(CheckLoginUserResponse.from(userService.findById(id)));
    }
}
