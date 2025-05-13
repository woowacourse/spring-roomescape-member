package roomescape.presentation.member.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.service.member.MemberService;
import roomescape.config.AccessToken;
import roomescape.config.CurrentLoginMember;
import roomescape.config.LoginMember;
import roomescape.presentation.member.dto.LoginCheckResponseDto;
import roomescape.presentation.member.dto.LoginRequestDto;
import roomescape.presentation.member.dto.MemberRequestDto;
import roomescape.presentation.member.dto.MemberResponseDto;

@RestController
public final class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> signup(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        Long memberId = memberService.registerMember(memberRequestDto);
        return ResponseEntity.created(URI.create("/members/" + memberId)).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequestDto loginRequestDto,
                                      HttpServletResponse response) {
        AccessToken accessToken = memberService.login(loginRequestDto);
        setCookie(response, accessToken.getValue());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponseDto> checkLogin(@CurrentLoginMember LoginMember loginMember) {
        return ResponseEntity.ok(new LoginCheckResponseDto(memberService.checkLogin(loginMember)));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        clearCookie(response);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponseDto>> getMembers() {
        List<MemberResponseDto> members = memberService.getMembers();
        return ResponseEntity.ok(members);
    }

    private void setCookie(HttpServletResponse response, String accessToken) {
        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "Strict");
        response.addCookie(cookie);
    }

    private void clearCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setAttribute("SameSite", "Strict");
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
