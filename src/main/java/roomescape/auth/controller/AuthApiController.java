package roomescape.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.AdminLoginRequest;
import roomescape.auth.dto.MemberLoginRequest;
import roomescape.auth.infrastructure.jwt.JwtHandler;
import roomescape.auth.service.AuthService;

@RestController
public class AuthApiController {

    private final AuthService authService;
    private final JwtHandler jwtHandler;

    public AuthApiController(AuthService authService, JwtHandler jwtHandler) {
        this.authService = authService;
        this.jwtHandler = jwtHandler;
    }

    @PostMapping("/members/login")
    public ResponseEntity<Void> userLogin(
        @RequestBody @Valid MemberLoginRequest memberLoginRequest,
        HttpServletResponse httpServletResponse
    ) {
        String token = authService.createMemberToken(memberLoginRequest);
        jwtHandler.setJwt(httpServletResponse, token);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/admins/login")
    public ResponseEntity<Void> adminLogin(
        @RequestBody @Valid AdminLoginRequest adminLoginRequest,
        HttpServletResponse httpServletResponse
    ) {
        String token = authService.createAdminToken(adminLoginRequest);
        jwtHandler.setJwt(httpServletResponse, token);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/members/logout")
    public ResponseEntity<Void> userLogout(
        HttpServletResponse httpServletResponse
    ) {
        jwtHandler.removeJwt(httpServletResponse);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/admins/logout")
    public ResponseEntity<Void> adminLogout(
        HttpServletResponse httpServletResponse
    ) {
        jwtHandler.removeJwt(httpServletResponse);

        return ResponseEntity.ok().build();
    }
}
