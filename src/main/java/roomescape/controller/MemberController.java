package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.controller.request.RegisterMemberRequest;
import roomescape.controller.response.RegisterUserResponse;
import roomescape.service.MemberService;
import roomescape.service.param.RegisterMemberParam;
import roomescape.service.result.MemberResult;

@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    @PostMapping("/members") //TODO:응답 형식 고려
    public ResponseEntity<RegisterUserResponse> signup(@RequestBody final RegisterMemberRequest registerMemberRequest) {
        RegisterMemberParam registerMemberParam = registerMemberRequest.toServiceParam();
        MemberResult memberResult = memberService.create(registerMemberParam);
        return ResponseEntity.ok(RegisterUserResponse.from(memberResult));
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
}
