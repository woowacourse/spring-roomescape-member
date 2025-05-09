package roomescape.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.service.MemberService;
import roomescape.config.LoginMember;
import roomescape.presentation.dto.LoginCheckResponseDto;
import roomescape.presentation.dto.LoginRequestDto;
import roomescape.presentation.dto.MemberRequestDto;
import roomescape.presentation.dto.MemberResponseDto;

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
    public ResponseEntity<LoginCheckResponseDto> checkLogin(LoginMember loginMember) {
        return ResponseEntity.ok(new LoginCheckResponseDto(loginMember.name()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        // TODO: 토큰을 어떻게 만료시킬 것인가?
        String accessToken = extractTokenFromCookie(cookies);
        clearCookie(response);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponseDto>> getMembers() {
        List<MemberResponseDto> members = memberService.getMembers();
        return ResponseEntity.ok(members);
    }

    private void setCookie(HttpServletResponse response, String accessToken) {
        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private void clearCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
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
