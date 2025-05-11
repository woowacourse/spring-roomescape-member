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
import roomescape.dto.NameResponse;
import roomescape.model.user.Member;
import roomescape.model.user.Name;
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
        Member member = memberService.login(loginRequest.email(), loginRequest.password());
        String token = memberService.createToken(member.getEmail()).token();

        Cookie cookie = memberService.createCookie(token);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<NameResponse> checkLogin(HttpServletRequest request) throws BadRequestException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String email = memberService.extractEmailFromCookies(cookies);
            Name name = memberService.getNameByEmail(email);
            return ResponseEntity.ok(new NameResponse(name.getValue()));
        }
        throw new BadRequestException("인증 실패");
    }
}
