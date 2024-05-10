package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.exception.BadRequestException;
import roomescape.service.MemberService;
import roomescape.service.dto.request.LoginRequest;
import roomescape.service.dto.response.MemberResponse;

@RestController
public class MemberController {

    public static final String TOKEN_NAME = "token";

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String token = memberService.login(loginRequest);

        Cookie cookie = new Cookie(TOKEN_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponse> findMember(HttpServletRequest request) {
        String token = extractTokenFromCookie(request.getCookies());

        MemberResponse memberResponse = memberService.findMember(token);
        return ResponseEntity.ok(memberResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(TOKEN_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> findMembers() {
        List<MemberResponse> members = memberService.findAll();
        return ResponseEntity.ok(members);
    }

    private String extractTokenFromCookie(Cookie[] cookies) { //TODO NULL 예외처리 고민
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TOKEN_NAME)) {
                return cookie.getValue();
            }
        }

        throw new BadRequestException("올바르지 않은 토큰값입니다.");
    }
}
