package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.LoginRequest;
import roomescape.dto.UserNameResponse;
import roomescape.model.user.User;
import roomescape.model.user.UserName;
import roomescape.service.MemberService;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final MemberService memberService;

    public LoginController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> login(@RequestBody @Valid LoginRequest loginRequest,
                                      HttpServletResponse response) throws BadRequestException {
        User user = memberService.login(loginRequest.email(), loginRequest.password());
        String token = memberService.createToken(user.getEmail()).token();

        Cookie cookie = memberService.createCookie(token);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<UserNameResponse> checkLogin(HttpServletRequest request) throws BadRequestException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String userEmail = memberService.extractUserEmailFromCookies(cookies);
            // userEmail로 user 이름 반환
            UserName userName = memberService.getUserNameByUserEmail(userEmail);
            return ResponseEntity.ok(new UserNameResponse(userName.getValue()));
        }
        throw new BadRequestException("인증 실패");
    }
}
