package roomescape.member.controller;

import java.util.List;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.common.exception.MissingLoginException;
import roomescape.member.controller.dto.LoginMemberCheckResponse;
import roomescape.member.controller.dto.MemberResponse;
import roomescape.member.service.AuthService;
import roomescape.member.service.MemberService;
import roomescape.member.service.dto.MemberLoginCommand;
import roomescape.member.service.dto.LoginMemberInfo;

/**
 * TODO
 * 로그인 관련과 Member 리소스 관련 Controller 분리?
 */
@RestController
public class MemberController {

    private final AuthService authService;
    private final MemberService memberService;

    public MemberController(final AuthService authService, MemberService memberService) {
        this.authService = authService;
        this.memberService = memberService;
    }

    /**
     * TODO
     * 이미 쿠키에 토큰이 존재한다면?
     */
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody MemberLoginCommand request) {
        String token = authService.tokenLogin(request);
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .path("/")
                .build();
        return ResponseEntity.ok().header("Set-Cookie", cookie.toString()).build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginMemberCheckResponse> getLoginMember(
            @CookieValue(value = "token", required = false) String token
    ) {
        if (token == null) {
            throw new MissingLoginException();
        }
        LoginMemberInfo loginMemberInfo = authService.getLoginMemberInfoByToken(token);
        return ResponseEntity.ok().body(new LoginMemberCheckResponse(loginMemberInfo.name()));
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> getMembers() {
        List<MemberResponse> responses = memberService.findAll()
                .stream()
                .map(MemberResponse::new)
                .toList();
        return ResponseEntity.ok().body(responses);
    }
}
