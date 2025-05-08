package roomescape.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.customer.LoginRequestDto;
import roomescape.service.AuthenticationService;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("login")
    public void userLogin(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        authenticationService.verifyLogin(loginRequestDto);
    }
}
