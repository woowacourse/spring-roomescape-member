package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.controller.request.RegisterMemberRequest;
import roomescape.controller.response.MemberResponse;
import roomescape.controller.response.RegisterUserResponse;
import roomescape.service.MemberService;
import roomescape.service.param.RegisterMemberParam;
import roomescape.service.result.MemberResult;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping //TODO:응답 형식 고려
    public ResponseEntity<RegisterUserResponse> signup(@RequestBody final RegisterMemberRequest registerMemberRequest) {
        RegisterMemberParam registerMemberParam = registerMemberRequest.toServiceParam();
        MemberResult memberResult = memberService.create(registerMemberParam);
        return ResponseEntity.ok(RegisterUserResponse.from(memberResult));
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> findMembers() {
        List<MemberResponse> members = memberService.findAll().stream()
                .map(MemberResponse::from)
                .toList();
        
        return ResponseEntity.ok().body(members);
    }
}
