package roomescape.web.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.infrastructure.persistence.MemberRepository;
import roomescape.service.response.MemberResponse;

@RestController
@RequestMapping("/members")
class MemberController {

    private final MemberRepository repository;

    public MemberController(MemberRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<MemberResponse>> readMembers() {
        List<MemberResponse> responses = repository.findAll().stream()
                .map(MemberResponse::from)
                .toList();

        return ResponseEntity.ok(responses);
    }
}
