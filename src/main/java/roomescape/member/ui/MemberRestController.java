package roomescape.member.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.domain.RequiresRole;
import roomescape.member.application.MemberService;
import roomescape.member.domain.UserRole;
import roomescape.member.ui.dto.CreateMemberRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping
    @RequiresRole(userRoles = {UserRole.ADMIN, UserRole.MEMBER, UserRole.GUEST})
    public ResponseEntity<Void> create(@RequestBody CreateMemberRequest request) {
        memberService.create(request);

        // TODO: 프론트에서 OK 응답을 받도록 해놓아서 일단 OK로 처리. 다른 API와 일관되게 처리하려면 CREATED로 변경 필요
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/{id}")
    @RequiresRole(userRoles = {UserRole.ADMIN, UserRole.MEMBER})
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        memberService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
