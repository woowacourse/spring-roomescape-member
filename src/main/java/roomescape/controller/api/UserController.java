package roomescape.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import roomescape.auth.UserId;
import roomescape.dto.SignupRequest;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.LoginCheckResponse;
import roomescape.service.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public void login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String token = userService.login(request);
        Cookie cookie = new Cookie("token", token);
        response.addCookie(cookie);
    }

    /**
     * TODO
     * - Reservation 조회 시 join으로 사용자 정보 불러와야함. 객체 연관관계 재매핑해야함.
     */

    // TODO: 비로그인 상태인 경우 UserId(required=false)와 같이 변경(현재는 비로그인 상태 시 400 에러 발생: [ERROR] JWT String argument cannot be null or empty.)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/login/check")
    public LoginCheckResponse loginCheck(@UserId Long id) {
        return userService.loginCheck(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public void signup(@RequestBody SignupRequest request) {
        userService.signup(request);
    }
}
