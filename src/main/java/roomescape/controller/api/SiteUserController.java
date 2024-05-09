package roomescape.controller.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.dto.LogInRequest;
import roomescape.dto.ProfileNameResponse;
import roomescape.service.SiteUserService;

import java.util.Arrays;

@RestController
@RequestMapping("/login")
public class SiteUserController {

    private final SiteUserService siteUserService;

    public SiteUserController(SiteUserService siteUserService) {
        this.siteUserService = siteUserService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody LogInRequest logInRequest) {
        String token = "token=" + siteUserService.logIn(logInRequest);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, token)
                .build();
    }

    @GetMapping("/check") //todo: URI 변경
    public ResponseEntity<ProfileNameResponse> loginCheck(HttpServletRequest request) {
        String token = Arrays.stream(request.getCookies()).toList().stream()
                .filter(c -> c.getName().equals("token"))
                .findAny()
                .get().getValue();
        ProfileNameResponse name = siteUserService.getNameIfLogIn(token);

        return ResponseEntity.ok()
                .body(name);
    }
}
