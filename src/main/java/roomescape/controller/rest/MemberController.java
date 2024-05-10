package roomescape.controller.rest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.login.LoginRequest;
import roomescape.service.MemberService;

@RestController
@RequestMapping("/login")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        String accessToken = memberService.login(loginRequest);

        // TODO: cookie 생성 로직이 controller에 있는게 맞는지 생각해보기
        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        // TODO: 응답 어떻게 주는게 좋은지 생각해보기
        return ResponseEntity.ok().build();
    }
}
