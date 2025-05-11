package roomescape.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.service.AuthenticationService;
import roomescape.exception.MemberException;
import roomescape.presentation.dto.LoginRequestDto;
import roomescape.presentation.dto.MemberCheckResponseDto;

@RestController
public class MemberController {

    private final AuthenticationService authenticationService;

    @Autowired
    public MemberController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        String token = authenticationService.login(loginRequest);
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .path("/")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberCheckResponseDto> loginCheck(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            // todo: 401 exception
            throw new MemberException("토큰이 존재하지 않습니다.");
        }
        // todo: 401 exception
        String token = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("token"))
                .findFirst()
                .orElseThrow(() -> new MemberException("토큰이 존재하지 않습니다."))
                .getValue();
        MemberCheckResponseDto memberDto = authenticationService.findMemberByToken(token);
        return ResponseEntity.ok().body(memberDto);
    }
}
