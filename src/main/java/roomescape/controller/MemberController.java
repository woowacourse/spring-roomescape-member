package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import roomescape.dto.request.MemberCreateRequest;
import roomescape.service.MemberService;

@RestController
public class MemberController {
    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    @PostMapping("/members")
    public ResponseEntity<Void> createMember(@RequestBody MemberCreateRequest request) {
        service.createMember(request);

        return ResponseEntity.noContent().build();
    }
}
