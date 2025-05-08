package roomescape.presentation.controller;

import jakarta.servlet.http.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.application.service.TokenLoginService;
import roomescape.domain.model.Member;
import roomescape.presentation.annotation.MemberAuthorization;
import roomescape.presentation.dto.request.TokenRequest;
import roomescape.presentation.dto.response.MemberNameResponse;

@Controller
public class LoginController {

    private final TokenLoginService tokenLoginService;

    public LoginController(final TokenLoginService tokenLoginService) {
        this.tokenLoginService = tokenLoginService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Validated @RequestBody TokenRequest request) {
        String accessToken = tokenLoginService.login(request);

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Keep-Alive", "timeout=" + 60);
        headers.add("Set-Cookie", "token=" + accessToken + "; Path=/; HttpOnly");

        return ResponseEntity.ok()
                .headers(headers)
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberNameResponse> check(@MemberAuthorization Member member) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Keep-Alive", "timeout=" + 60);
        headers.add("Transfer-Encoding", "chunked");

        MemberNameResponse response = new MemberNameResponse(member.getName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        Cookie cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "token=" + cookie.getValue() + "; Path=/; HttpOnly");

        return ResponseEntity.ok()
                .headers(headers)
                .build();
    }

}
