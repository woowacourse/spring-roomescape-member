package roomescape.auth.controller;

import java.net.URI;
import java.util.Date;

import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import roomescape.auth.domain.Member;
import roomescape.auth.dto.MemberLoginRequestDto;
import roomescape.auth.dto.MemberResponseDto;
import roomescape.auth.dto.MemberSignUpRequestDto;
import roomescape.auth.service.AuthService;
import roomescape.configuration.AuthenticatedMember;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid MemberLoginRequestDto memberLoginRequestDto) {
        String token = authService.login(memberLoginRequestDto);
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .path("/")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().headers(headers).build();
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody @Valid MemberSignUpRequestDto memberSignUpRequestDto) {
        long id = authService.signUp(memberSignUpRequestDto);
        return ResponseEntity.created(URI.create("/signup/" + id)).build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberResponseDto> loginCheck(@AuthenticatedMember Member member) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.DATE, new Date().toString());
        return ResponseEntity.ok().headers(headers).body(new MemberResponseDto(member.getName()));
    }
}
