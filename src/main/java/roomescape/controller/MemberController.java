package roomescape.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import roomescape.dto.request.MemberCreateRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.service.MemberService;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> readMembers() {
        List<MemberResponse> response = service.readMember();
        return ResponseEntity.ok(response);

    }

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody MemberCreateRequest request) {
        MemberResponse response = service.createMember(request);

        URI location = URI.create("/members/" + response.id());
        return ResponseEntity.created(location).build();
    }
}
