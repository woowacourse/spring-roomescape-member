package roomescape.auth.login.presentation.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.login.infrastructure.JwtTokenManager;
import roomescape.auth.login.presentation.controller.dto.annotation.LoginAdmin;
import roomescape.auth.login.presentation.controller.dto.LoginAdminInfo;
import roomescape.auth.login.presentation.controller.dto.LoginCheckResponse;
import roomescape.auth.login.presentation.controller.dto.LoginRequest;
import roomescape.admin.domain.Admin;
import roomescape.admin.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminLoginController {

    private final AdminService adminService;

    public AdminLoginController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request) {
        boolean adminExist = adminService.isExistsByEmail(request.email());

        if (!adminExist) {
            return ResponseEntity.status(401).build();
        }

        Admin admin = adminService.findByEmail(request.email());
        if (!admin.isSamePassword(request.password())) {
            return ResponseEntity.status(401).build();
        }

        String token = JwtTokenManager.crateToken(admin.getId(), "ADMIN");

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> checkLogin(@LoginAdmin final LoginAdminInfo info) {
        Admin admin = adminService.findById(info.id());
        return ResponseEntity.ok().body(new LoginCheckResponse(admin.getName()));
    }
}
