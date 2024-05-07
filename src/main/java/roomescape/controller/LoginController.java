package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.request.UserLoginRequest;
import roomescape.controller.response.UserLoginResponse;
import roomescape.service.MemberService;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final MemberService memberService;

    public LoginController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<UserLoginResponse> save(@RequestBody UserLoginRequest userLoginRequest) {
        UserLoginResponse tokenResponse = memberService.createToken(userLoginRequest);
        return ResponseEntity.ok().body(tokenResponse);
    }
}
