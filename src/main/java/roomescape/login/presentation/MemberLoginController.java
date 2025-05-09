package roomescape.login.presentation;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.common.util.auth.JwtTokenManager;
import roomescape.login.presentation.dto.LoginCheckResponse;
import roomescape.login.presentation.dto.LoginRequest;
import roomescape.member.domain.Member;
import roomescape.member.service.MemberService;

@RestController
public class MemberLoginController {

    private final MemberService memberService;

    public MemberLoginController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        boolean userExist = memberService.isExistsByEmail(request.email());

        if (!userExist) {
            return ResponseEntity.status(401).build();
        }

        Member member = memberService.findByEmail(request.email());
        if (!member.isSamePassword(request.password())) {
            return ResponseEntity.status(401).build();
        }

        String token = JwtTokenManager.crateToken(member.getId(), "USER");

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> checkLogin(final HttpServletRequest request) {
        if (request.getCookies() == null) {
            throw new IllegalStateException("인증할 수 없습니다.");
        }

        String token = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("token"))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("인증할 수 없습니다."))
                .getValue();

        Long id = JwtTokenManager.getId(token);

        Member member = memberService.findById(id);
        return ResponseEntity.ok().body(new LoginCheckResponse(member.getName()));
    }
}
