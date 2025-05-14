package roomescape.member.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.domain.Visitor;
import roomescape.member.dto.MemberLoginRequest;
import roomescape.member.dto.MemberNameResponse;
import roomescape.member.dto.MemberResponse;
import roomescape.member.dto.MemberSignUpResponse;
import roomescape.member.dto.MemberSignupRequest;
import roomescape.member.service.MemberService;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberSignUpResponse> signup(@Valid @RequestBody MemberSignupRequest memberSignupRequest) {
        memberService.signup(memberSignupRequest);
        return ResponseEntity.created(URI.create("/login")).body(MemberSignUpResponse.ofSuccess());
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> getAllUsers() {
        List<MemberResponse> memberResponses = memberService.findAllUsers();
        return ResponseEntity.ok(memberResponses);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody MemberLoginRequest memberLoginRequest) {
        String jwtToken = memberService.login(memberLoginRequest);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, setCookie("token", jwtToken).toString()).build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberNameResponse> checkUserLogin(Visitor visitor) {
        MemberNameResponse memberNameResponse = memberService.checkUserLogin(visitor);
        return ResponseEntity.ok(memberNameResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, removeCookie("token").toString()).build();
    }

    private ResponseCookie setCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .build();
    }

    private ResponseCookie removeCookie(String name) {
        return ResponseCookie.from(name, null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
    }
}
