package roomescape.member;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.dto.MemberRequest;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(
            final MemberService memberService
    ) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Void> createMember(
            @RequestBody @Valid MemberRequest request
    ) {
        memberService.createMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
