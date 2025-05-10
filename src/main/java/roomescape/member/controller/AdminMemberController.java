package roomescape.member.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.controller.dto.MemberResponse;
import roomescape.member.service.AdminMemberService;
import roomescape.member.service.dto.response.MemberServiceResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/members")
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    @GetMapping
    public List<MemberResponse> getAll() {
        List<MemberServiceResponse> responses = adminMemberService.getAll();
        return responses.stream()
                .map(MemberResponse::from)
                .toList();
    }
}
