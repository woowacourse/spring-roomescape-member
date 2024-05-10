package roomescape.controller.api;

import static roomescape.controller.TokenExtractor.extractTokenFromCookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.request.MemberSignUpRequest;
import roomescape.controller.request.TokenWebRequest;
import roomescape.controller.response.MemberWebResponse;
import roomescape.service.JwtProvider;
import roomescape.service.MemberAuthService;
import roomescape.service.request.MemberSignUpAppRequest;
import roomescape.service.request.TokenAppRequest;
import roomescape.service.response.MemberAppResponse;

@RestController
public class MemberAuthController {

    private final MemberAuthService memberAuthService;
    private final JwtProvider jwtProvider;

    public MemberAuthController(MemberAuthService memberAuthService, JwtProvider jwtProvider) {
        this.memberAuthService = memberAuthService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody TokenWebRequest request,
                                      HttpServletResponse response) {
        String token = jwtProvider.createToken(new TokenAppRequest(request.email(), request.password()));
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberWebResponse> findMember(HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw new IllegalArgumentException("쿠키가 없습니다. 다시 로그인 해주세요.");
        }
        String token = extractTokenFromCookie(request.getCookies());
        String email = jwtProvider.getPayload(token);
        MemberAppResponse appResponse = memberAuthService.findMemberByEmail(email);
        MemberWebResponse response = new MemberWebResponse(appResponse.id(), appResponse.name(), appResponse.role());

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberWebResponse> signUp(@Valid @RequestBody MemberSignUpRequest request) {
        MemberAppResponse appResponse = memberAuthService.signUp(
            new MemberSignUpAppRequest(request.name(), request.email(), request.password()));

        MemberWebResponse response = new MemberWebResponse(appResponse.id(), appResponse.name(), appResponse.role());
        return ResponseEntity.created(URI.create("/member" + appResponse.id())).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String token = extractTokenFromCookie(request.getCookies());
        String expiredToken = jwtProvider.createExpiredToken(token);
        Cookie cookie = new Cookie("token", expiredToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberWebResponse>> getMembers() {
        List<MemberAppResponse> appResponses = memberAuthService.findAll();

        List<MemberWebResponse> responses = appResponses.stream()
            .map(memberAppResponse -> new MemberWebResponse(
                memberAppResponse.id(),
                memberAppResponse.name(),
                memberAppResponse.role())
            )
            .toList();

        return ResponseEntity.ok().body(responses);
    }
}
