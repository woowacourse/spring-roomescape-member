package roomescape.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.LoginMemberArgument;
import roomescape.domain.Member;
import roomescape.dto.LoginRequest;
import roomescape.dto.MemberResponse;
import roomescape.service.MemberService;
import roomescape.util.JwtTokenProvider;

@RestController
public class LoginController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginController(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public void tokenLogin(@RequestBody LoginRequest request, HttpServletResponse response) {
        Member member = memberService.getMemberByLogin(request);
        String accessToken = jwtTokenProvider.createToken(member);

        Cookie cookie = new Cookie(JwtTokenProvider.TOKEN_NAME, accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    @GetMapping("/login/member")
    public ResponseEntity<MemberResponse> checkLogin(@LoginMemberArgument Member member) {
        return ResponseEntity.ok()
                .body(MemberResponse.from(member));
    }
}
