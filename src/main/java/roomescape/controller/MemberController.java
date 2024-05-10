package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.UserLoginRequest;
import roomescape.dto.request.UserSignUpRequest;
import roomescape.dto.response.CheckMemberResponse;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.TokenResponse;
import roomescape.service.MemberService;
import roomescape.ui.MemberIdConverter;

@RestController
public class MemberController {
    private static final String TOKEN = "token";

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> findAll() {
        List<MemberResponse> memberResponses = memberService.findAll();

        return ResponseEntity.ok(memberResponses);
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> save(@RequestBody @Valid UserSignUpRequest userSignUpRequest) {
        MemberResponse memberResponse = memberService.save(userSignUpRequest);
        return ResponseEntity.created(URI.create("/members/" + memberResponse.id()))
                .body(memberResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            HttpServletResponse response,
            @RequestBody @Valid UserLoginRequest userLoginRequest
    ) {
        TokenResponse tokenResponse = memberService.createToken(userLoginRequest);
        Cookie cookie = new Cookie(TOKEN, tokenResponse.token());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/login/check")
    public ResponseEntity<CheckMemberResponse> checkLogin(@MemberIdConverter Long memberId) {
        CheckMemberResponse checkMemberResponse = memberService.findById(memberId);
        return ResponseEntity.ok(checkMemberResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(TOKEN, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
