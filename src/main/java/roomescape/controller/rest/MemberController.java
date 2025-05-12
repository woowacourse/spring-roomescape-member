package roomescape.controller.rest;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.JwtProvider;
import roomescape.dto.request.LoginMember;
import roomescape.dto.request.LoginMemberRequest;
import roomescape.dto.request.SignUpRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.global.resolver.CurrentMember;
import roomescape.service.MemberService;

@RestController
@RequestMapping()
public class MemberController {
    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    public MemberController(final JwtProvider jwtProvider, final MemberService memberService) {
        this.jwtProvider = jwtProvider;
        this.memberService = memberService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Void> loginUser(@RequestBody LoginMemberRequest loginMemberRequest,
                                          HttpServletResponse response) {
        LoginMember loginMember = memberService.loginMember(loginMemberRequest);
        String accessToken = jwtProvider.generateToken(loginMember);
        Cookie cookie = createCookie(accessToken);
        response.addCookie(cookie);
        response.setHeader("Keep-Alive", "timeout=60");
        return ResponseEntity.ok().build();

    }

    @GetMapping("/auth/login/check")
    public ResponseEntity<MemberResponse> loginCheck(@CurrentMember LoginMember member) {
        return ResponseEntity.ok(new MemberResponse(member.id(), member.name().getName()));
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> findAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        MemberResponse memberResponse = memberService.signUp(signUpRequest);
        URI location = URI.create("/users/" + memberResponse.id());
        return ResponseEntity.created(location)
                .body(memberResponse);
    }

    private Cookie createCookie(final String accessToken) {
        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}

