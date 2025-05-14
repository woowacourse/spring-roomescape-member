package roomescape.controller.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import roomescape.dto.request.MemberPostRequest;
import roomescape.dto.response.MemberSafeResponse;
import roomescape.service.MemberService;
import roomescape.web.LoginMember;

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

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(
            LoginMember loginMember,
            HttpServletResponse response
    ) {
        memberService.deleteUser(loginMember);

        Cookie expiredCookie = new Cookie("token", null);
        expiredCookie.setHttpOnly(true);
        expiredCookie.setPath("/");
        expiredCookie.setMaxAge(0);
        response.addCookie(expiredCookie);
    }
}
