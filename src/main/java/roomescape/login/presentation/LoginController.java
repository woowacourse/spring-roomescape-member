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
import roomescape.member.domain.Admin;
import roomescape.member.domain.Member;
import roomescape.member.service.AdminService;
import roomescape.member.service.MemberService;

@RestController
public class LoginController {

    private final AdminService adminService;
    private final MemberService memberService;

    public LoginController(AdminService adminService, MemberService memberService) {
        this.adminService = adminService;
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        boolean adminExist = adminService.isExistsByEmail(request.email());
        boolean userExist = memberService.isExistsByEmail(request.email());

        if (!adminExist && !userExist) {
            return ResponseEntity.status(401).build();
        }

        String token = null;
        if (userExist) {
            Member member = memberService.findByEmail(request.email());
            if (!member.isSamePassword(request.password())) {
                return ResponseEntity.status(401).build();
            }

            token = JwtTokenManager.crateToken(member.getId(), "USER");
        }
        if (adminExist) {
            Admin admin = adminService.findByEmail(request.email());
            if (!admin.isSamePassword(request.password())) {
                return ResponseEntity.status(401).build();
            }

            token = JwtTokenManager.crateToken(admin.getId(), "ADMIN");
        }

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
        String role = JwtTokenManager.getRole(token);

        if (role.equals("ADMIN")) {
            Admin admin = adminService.findById(id);
            return ResponseEntity.ok().body(new LoginCheckResponse(admin.getName()));
        }

        Member member = memberService.findById(id);
        return ResponseEntity.ok().body(new LoginCheckResponse(member.getName()));
    }
}
