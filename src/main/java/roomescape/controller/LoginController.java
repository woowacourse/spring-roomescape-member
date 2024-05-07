package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody final MemberLoginRequest memberLoginRequest, final HttpServletResponse response) {
        // TODO cookie 예쁘게 굽기
        final Cookie cookie = new Cookie("token", memberLoginRequest.email());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.setHeader("Keep-Alive", "timeout=600");
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/check")
    public ResponseEntity<MemberResponse> check(final HttpServletRequest request) {
        final MemberResponse member = getMember(request);
        return ResponseEntity.ok(member);
    }

    private MemberResponse getMember(final HttpServletRequest request) {
        //TODO Cannot read the array length because "<local3>" is null 뭔진 몰겠지만 이런 예외 발생
        final Cookie[] cookies = request.getCookies();
        for (final Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return new MemberResponse(cookie.getValue());
            }
        }
        return null; //TODO 예외 던지기
    }
}
