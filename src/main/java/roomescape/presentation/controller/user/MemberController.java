package roomescape.presentation.controller.user;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.business.LoginInformation;
import roomescape.business.service.AuthenticationService;
import roomescape.business.service.MemberService;
import roomescape.presentation.dto.request.LoginRequestDto;
import roomescape.presentation.dto.response.MemberCheckResponseDto;
import roomescape.presentation.dto.response.MemberResponseDto;

@RestController
public class MemberController {

    private final AuthenticationService authenticationService;
    private final MemberService memberService;

    @Autowired
    public MemberController(AuthenticationService authenticationService, MemberService memberService) {
        this.authenticationService = authenticationService;
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        String token = authenticationService.login(loginRequest);
        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .path("/")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<MemberCheckResponseDto> loginCheck(LoginInformation loginInformation) {
        return ResponseEntity.ok()
                .body(new MemberCheckResponseDto(loginInformation.name()));
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponseDto>> readMembers() {
        List<MemberResponseDto> members = memberService.readMemberAll();
        return ResponseEntity.ok()
                .body(members);
    }
}
