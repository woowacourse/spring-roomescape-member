package roomescape.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.infrastructure.argument.AuthorizedMember;
import roomescape.member.domain.Member;
import roomescape.member.dto.NameResponse;

@RestController
@RequestMapping("/members")
public class MemberApiController {

    @GetMapping("/my/name")
    public ResponseEntity<NameResponse> getMyName(@AuthorizedMember Member authorizedMember) {
        return ResponseEntity.ok().body(NameResponse.fromMember(authorizedMember));
    }
}
