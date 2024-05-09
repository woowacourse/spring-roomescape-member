package roomescape.controller.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.LogInRequest;
import roomescape.service.SiteUserService;

@RestController
public class SiteUserController {

    private final SiteUserService siteUserService;

    public SiteUserController(SiteUserService siteUserService) {
        this.siteUserService = siteUserService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LogInRequest logInRequest) {
        String token = "token=" + siteUserService.logIn(logInRequest);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, token)
                .build();
    }
}
