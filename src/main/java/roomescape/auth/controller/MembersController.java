package roomescape.auth.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.service.MemberService;
import roomescape.auth.service.dto.request.UserSignupRequest;
import roomescape.auth.service.dto.response.MemberBasicInfoResponse;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MembersController {
    private final MemberService service;

    public MembersController(MemberService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> signup(@RequestBody @Valid UserSignupRequest request) {
        service.signup(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<MemberBasicInfoResponse>> getAllMembers() {
        List<MemberBasicInfoResponse> responses = service.getAllMemberNames();
        return ResponseEntity.ok(responses);
    }
}
