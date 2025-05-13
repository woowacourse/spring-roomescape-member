package roomescape.controller.rest;

import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
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
import roomescape.global.util.CookieUtils;
import roomescape.service.MemberService;

@RestController
@RequestMapping
public class LoginController {
    private final JwtProvider jwtProvider;
    private final MemberService memberService;
    private final CookieUtils cookieUtils;

    public LoginController(final JwtProvider jwtProvider, final MemberService memberService,
                           final CookieUtils cookieUtils) {
        this.jwtProvider = jwtProvider;
        this.memberService = memberService;
        this.cookieUtils = cookieUtils;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Void> loginUser(@RequestBody LoginMemberRequest request,
                                          HttpServletResponse response) {
        LoginMember loginMember = memberService.loginMember(request);
        String accessToken = jwtProvider.generateToken(loginMember);
        cookieUtils.addTokenCookie(response, accessToken);
        response.setHeader("Keep-Alive", "timeout=60");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/auth/login/check")
    public ResponseEntity<MemberResponse> loginCheck(@CurrentMember LoginMember member) {
        return ResponseEntity.ok(new MemberResponse(member.id(), member.name().getName()));
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        MemberResponse memberResponse = memberService.signUp(signUpRequest);
        URI location = URI.create("/members/" + memberResponse.id());
        return ResponseEntity.created(location)
                .body(memberResponse);
    }

    @PostMapping("/adminSignup")
    public ResponseEntity<MemberResponse> adminSignUp(@RequestBody SignUpRequest signUpRequest) {
        MemberResponse memberResponse = memberService.adminSignUp(signUpRequest);
        URI location = URI.create("/members/" + memberResponse.id());
        return ResponseEntity.created(location)
                .body(memberResponse);
    }
}

