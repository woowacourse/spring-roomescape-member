package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.RegistrationRequestDto;
import roomescape.dto.TokenResponseDto;
import roomescape.service.SignupService;

@RestController
public class AuthController {

    private final SignupService signupService;

    public AuthController(SignupService signupService) {
        this.signupService = signupService;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> registerMember(@Valid @RequestBody RegistrationRequestDto registrationRequestDto, HttpServletResponse response) {
        TokenResponseDto tokenResponseDto = signupService.signup(registrationRequestDto);
        response.addCookie(new Cookie("token", tokenResponseDto.accessToken()));
        return ResponseEntity.ok().build();
    }
}
