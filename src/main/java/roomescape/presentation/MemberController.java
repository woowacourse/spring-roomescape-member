package roomescape.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.service.MemberService;
import roomescape.presentation.dto.LoginCheckResponseDto;
import roomescape.presentation.dto.LoginRequestDto;
import roomescape.presentation.dto.MemberRequestDto;

@RestController
public final class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> signup(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        Long memberId = memberService.registerMember(memberRequestDto);
        return ResponseEntity.created(URI.create("/members/" + memberId)).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequestDto loginRequestDto,
                                      HttpServletResponse response) {
        String accessToken = memberService.login(loginRequestDto);
        setCookie(response, accessToken);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponseDto> checkLogin(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String accessToken = extractTokenFromCookie(cookies);
        return ResponseEntity.ok(memberService.getMemberFromToken(accessToken));
    }

    private void setCookie(HttpServletResponse response, String accessToken) {
        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
