package roomescape.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.common.annotation.Auth;
import roomescape.domain.member.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.repository.MemberRepository;
import roomescape.service.AuthService;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final MemberRepository memberRepository;
    private final AuthService authService;

    @Autowired
    public LoginController(MemberRepository memberRepository, AuthService authService) {
        this.memberRepository = memberRepository;
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<Void> loginAndIssueToken(@RequestBody LoginRequest request, HttpServletResponse response) {
        // TODO: 회원 관련 예외에 대한 커스텀 익셉션 추가
        Member member = memberRepository.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 회원입니다."));

        Cookie cookie = authService.generateTokenCookie(member);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/check")
    public ResponseEntity<Member> checkToken(@Auth Member member) {
        return ResponseEntity.ok(member);
    }
}
