package roomescape.member.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberToken;
import roomescape.member.dto.LogInRequest;
import roomescape.member.dto.MemberInfo;
import roomescape.member.service.AuthService;
import java.util.List;

@Controller
public class MemberController {

    private final AuthService authService;

    public MemberController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> postLogin(HttpServletResponse response, @RequestBody LogInRequest logInRequest) {
        MemberToken token = authService.createToken(logInRequest);
        Cookie cookie = new Cookie("token", token.asString());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberInfo> getLoginInfo(Member member) {
        MemberInfo loginInfo = new MemberInfo(member.getId(), member.getName());
        return ResponseEntity.ok(loginInfo);
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberInfo>> getMembers() {
        List<MemberInfo> members = authService.findAll().stream()
                .map(member -> new MemberInfo(member.getId(), member.getName()))
                .toList();
        return ResponseEntity.ok(members);
    }
}
