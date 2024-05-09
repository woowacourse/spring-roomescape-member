package roomescape.web.controller;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import roomescape.service.MemberService;
import roomescape.web.dto.request.LoginRequest;
import roomescape.web.dto.response.MemberResponse;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest request) {
        String jwtToken = memberService.login(request);

        return ResponseEntity.ok()
                .header(SET_COOKIE, jwtToken)
                .build();
    }

    @GetMapping("/check")
    public ResponseEntity<MemberResponse> findAuthenticatedMember(
            @CookieValue(value = "token", defaultValue = "") String token) {
        MemberResponse response = memberService.findMemberByToken(token);

        return ResponseEntity.ok()
                .body(response);
    }
}
