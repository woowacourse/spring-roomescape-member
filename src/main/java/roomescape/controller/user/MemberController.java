package roomescape.controller.user;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.MemberPostRequest;
import roomescape.dto.response.MemberSafeResponse;
import roomescape.service.MemberService;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MemberSafeResponse> readMembers() {
        return memberService.findAllMembers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberSafeResponse createMember(
            @RequestBody MemberPostRequest request
    ) {
        return memberService.createUser(request);
    }
}
