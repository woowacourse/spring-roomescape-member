package roomescape.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.TokenRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.TokenResponse;
import roomescape.service.MemberService;
import roomescape.support.auth.AuthorizationExtractor;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final MemberService memberService;
    private final AuthorizationExtractor authorizationExtractor;

    public LoginController(MemberService memberService, AuthorizationExtractor authorizationExtractor) {
        this.memberService = memberService;
        this.authorizationExtractor = authorizationExtractor;
    }

    @PostMapping()
    public ResponseEntity<Void> create(
            @RequestBody @Valid final TokenRequest tokenRequest) {
        final TokenResponse tokenResponse = memberService.createToken(tokenRequest);

        final HttpCookie cookie = ResponseCookie.from("token", tokenResponse.accessToken())
                .path("/")
                .httpOnly(true)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/check")
    public ResponseEntity<MemberResponse> findInfo(final HttpServletRequest request) {
        final String token = authorizationExtractor.extract(request);
        final MemberResponse memberResponse = memberService.findMemberByToken(token);
        return ResponseEntity.ok().body(memberResponse);
    }
}
